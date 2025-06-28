console.log('app.js загружен!');

document.getElementById('check-symbol-btn').onclick = async function() {
  const exchange = document.getElementById('main-exchange').value;
  let symbol = document.getElementById('main-symbol').value.trim();
  const statusDiv = document.getElementById('status');

  if (!symbol) {
      statusDiv.className = 'status-message error';
      statusDiv.textContent = 'Введите символ!';
      return;
  }

  statusDiv.className = 'status-message processing';
  statusDiv.textContent = 'Проверка символа...';

  try {
      const response = await fetch('/api/check-symbol', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ exchange, symbol })
      });
      const result = await response.json();

      if (result.ok) {
          statusDiv.className = 'status-message success';
          statusDiv.textContent = 'Такой символ есть на бирже!';
      } else {
          statusDiv.className = 'status-message error';
          statusDiv.textContent = 'Такого символа нет на бирже!';
      }
  } catch (e) {
      statusDiv.className = 'status-message error';
      statusDiv.textContent = 'Ошибка проверки символа!';
  }
};

function formatDateTimeLocal(dt) {
    return dt ? dt.replace('T', ' ') : '';
}

document.getElementById('download-btn').onclick = async function() {
    console.log('Кнопка нажата!');
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;
    let symbol = document.getElementById('main-symbol').value.trim();
    const interval = document.getElementById('interval').value;
    const paramSelects = document.querySelectorAll('.param-select');
    const exchangeSelects = document.querySelectorAll('.exchange-select');
    const columns = [];
    for (let i = 0; i < paramSelects.length; i++) {
        columns.push({
            type: paramSelects[i].value,
            exchange: exchangeSelects[i].value.toLowerCase()
        });
    }

    const statusDiv = document.getElementById('status');

    if (!startTime || !endTime || !symbol || !interval || columns.length === 0) {
        statusDiv.className = 'status-message error';
        statusDiv.textContent = 'Заполните все поля!';
        return;
    }

    const data = {
        startTime: formatDateTimeLocal(startTime),
        endTime: formatDateTimeLocal(endTime),
        symbol,
        interval,
        columnsCount: columns.length,
        columns: columns
    };
    statusDiv.className = 'status-message processing';
    statusDiv.textContent = 'Генерация и скачивание файла...';
    try {
        const response = await fetch('/api/export-table', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            throw new Error('Ошибка сервера');
        }
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'export.xlsx';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        statusDiv.className = 'status-message success';
        statusDiv.textContent = 'Файл успешно сгенерирован и скачан!';
    } catch (e) {
        statusDiv.className = 'status-message error';
        statusDiv.textContent = 'Ошибка при скачивании файла!';
    }
};