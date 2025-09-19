package Exchange.service;

import Exchange.dto.ExportRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {
    
    public void validateExportRequest(ExportRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть пустым");
        }
        
        if (request.getSymbol() == null || request.getSymbol().trim().isEmpty()) {
            throw new IllegalArgumentException("Символ не может быть пустым");
        }
        
        if (request.getInterval() == null || request.getInterval().trim().isEmpty()) {
            throw new IllegalArgumentException("Интервал не может быть пустым");
        }
        
        if (request.getStartTime() == null || request.getStartTime().trim().isEmpty()) {
            throw new IllegalArgumentException("Время начала не может быть пустым");
        }
        
        if (request.getEndTime() == null || request.getEndTime().trim().isEmpty()) {
            throw new IllegalArgumentException("Время окончания не может быть пустым");
        }
        
        if (request.getColumns() == null || request.getColumns().isEmpty()) {
            throw new IllegalArgumentException("Список столбцов не может быть пустым");
        }
        
        List<String> exchanges = request.getColumns().stream()
                .map(col -> col.getExchange().toLowerCase())
                .distinct()
                .toList();
                
        if (exchanges.isEmpty()) {
            throw new IllegalArgumentException("Не указаны биржи для экспорта");
        }
    }
} 