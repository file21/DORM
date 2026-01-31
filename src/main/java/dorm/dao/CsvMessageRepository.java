package dorm.dao;

import dorm.model.Message;
import dorm.util.CsvHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * CSV implementation of MessageRepository.
 * Demonstrates SRP - handles only message CSV operations.
 */
public class CsvMessageRepository implements MessageRepository {
    
    private static final String FILENAME = "messages.csv";
    private static final String HEADER = "id,from_user,to_user,content,sent_at,read";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public List<Message> findByUser(String username) {
        List<String[]> records = CsvHelper.readAll(FILENAME, true);
        List<Message> messages = new ArrayList<>();
        
        for (String[] record : records) {
            if (record.length >= 5) {
                String fromUser = record[1];
                String toUser = record[2];
                
                if (fromUser.equals(username) || toUser.equals(username)) {
                    messages.add(recordToMessage(record));
                }
            }
        }
        
        // Sort by sent_at descending
        messages.sort(Comparator.comparing(Message::getSentAt).reversed());
        
        return messages;
    }
    
    @Override
    public void save(Message message) {
        String[] record = messageToRecord(message);
        CsvHelper.append(FILENAME, HEADER, record);
    }
    
    @Override
    public void update(Message message) {
        List<String[]> records = CsvHelper.readAll(FILENAME, true);
        List<String[]> updated = new ArrayList<>();
        
        for (String[] record : records) {
            if (record.length >= 5 && record[0].equals(message.getId())) {
                updated.add(messageToRecord(message));
            } else {
                // Ensure old records have 6 fields
                if (record.length == 5) {
                    String[] newRecord = new String[6];
                    System.arraycopy(record, 0, newRecord, 0, 5);
                    newRecord[5] = "false";
                    updated.add(newRecord);
                } else {
                    updated.add(record);
                }
            }
        }
        
        CsvHelper.writeAll(FILENAME, HEADER, updated);
    }
    
    /**
     * Convert CSV record to Message object
     */
    private Message recordToMessage(String[] record) {
        LocalDateTime sentAt;
        try {
            sentAt = LocalDateTime.parse(record[4], FORMATTER);
        } catch (Exception e) {
            sentAt = LocalDateTime.now();
        }
        
        // Handle read field (may not exist in old records)
        boolean read = false;
        if (record.length >= 6) {
            read = "true".equalsIgnoreCase(record[5]);
        }
        
        return new Message(
            record[0],  // id
            record[1],  // from_user
            record[2],  // to_user
            record[3],  // content
            sentAt,     // sent_at
            read        // read
        );
    }
    
    /**
     * Convert Message to CSV record
     */
    private String[] messageToRecord(Message message) {
        return new String[] {
            message.getId(),
            message.getFromUser(),
            message.getToUser(),
            message.getContent(),
            message.getSentAt().format(FORMATTER),
            String.valueOf(message.isRead())
        };
    }
}
