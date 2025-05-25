package bybit.bybit_exel.Controller;

import bybit.bybit_exel.dto.ExportRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExportController {

    @PostMapping("/export-table")
    public ResponseEntity<?> exportTable(@RequestBody ExportRequest request) {
        /*
        реквест - обработка даты, запись столбцов и их параметров
        создание потоков для получения инфы с бирж
        обработка инфы в ConcurentHashMap (Timestemp, Queue<Object>) запись по времени, список данных
        перенос данных в таблицу ексель
         */
        // 1. Многопоточно собираете данные с бирж
        // 2. Генерируете Excel-файл
        // 3. Возвращаете файл потоком или ссылку на скачивание

        return ResponseEntity.ok().build();
    }
}
