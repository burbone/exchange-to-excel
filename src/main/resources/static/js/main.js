function normalizeSymbol(symbol) {
    return symbol.toUpperCase().trim().replace(/\s+/g, '');
}

function setupWebSocketConnection() {
    let ws = null;

    function connect() {
        try {
            ws = new WebSocket('ws://localhost:8081/ws');

            ws.onopen = function() {
                console.log('WebSocket connected to server');

                const pageName = getCurrentPageName();
                ws.send(JSON.stringify({
                    type: 'page_info',
                    page: pageName
                }));
            };

            ws.onclose = function() {
                console.log('WebSocket disconnected from server');
            };

            ws.onerror = function(error) {
                console.log('WebSocket error:', error);
            };

        } catch (error) {
            console.log('WebSocket connection failed:', error);
        }

        return ws;
    }

    function getCurrentPageName() {
        const path = window.location.pathname;
        if (path.includes('data')) return 'data';
        if (path.includes('index')) return 'index';
        if (path.includes('symbol')) return 'symbol';
        if (path.includes('online')) return 'online';
        if (path.includes('arbitrage')) return 'arbitrage';
        return 'index';
    }

    ws = connect();
    const pingInterval = setInterval(function() {
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify({
                type: 'ping',
                page: getCurrentPageName()
            }));
        }
    }, 3000);

    window.addEventListener('beforeunload', function() {
        clearInterval(pingInterval);
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify({
                type: 'navigation'
            }));
            setTimeout(() => {
                ws.close();
            }, 100);
        }
    });

    const links = document.querySelectorAll('a[href$=".html"]');
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            if (ws && ws.readyState === WebSocket.OPEN) {
                ws.send(JSON.stringify({
                    type: 'navigation'
                }));
            }
        });
    });
}

function setupBrowserCloseHandler() {
    window.addEventListener('beforeunload', function(e) {
    });
}

const ArbitrageModule = {
    API_BASE: 'http://localhost:8081/api',
    currentSymbol: '',

    updateLastUpdateTime: function() {
        const element = document.getElementById('lastUpdate');
        if (element) {
            element.textContent = new Date().toLocaleTimeString();
        }
    },

loadTop5: async function() {
        try {
            const response = await fetch(`${this.API_BASE}/top5`);
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }
            const data = await response.json();

            let html = '';
            if (data && data.length > 0) {
                html = `<div style="overflow-x: auto;">
                <table class="excel-table" style="min-width: 100%; white-space: nowrap;">
                    <thead>
                        <tr>
                            <th>Символ</th>
                            <th>Купить</th>
                            <th>Цена покупки</th>
                            <th>Продать</th>
                            <th>Цена продажи</th>
                            <th>Прибыль</th>
                        </tr>
                    </thead>
                    <tbody>`;

                data.forEach(item => {
                    html += `<tr>
                        <td><strong>${item.symbol}</strong></td>
                        <td>${item.buyExchange}</td>
                        <td>${item.buyPrice.toFixed(4)}</td>
                        <td>${item.sellExchange}</td>
                        <td>${item.sellPrice.toFixed(4)}</td>
                        <td class="positive">+${item.profitPercent.toFixed(3)}%</td>
                    </tr>`;
                });

                html += `</tbody></table>`;
            } else {
                html = 'Данные временно недоступны';
            }

            const element = document.getElementById('top5');
            if (element) {
                element.innerHTML = html;
            }
        } catch (error) {
            const element = document.getElementById('top5');
            if (element) {
                element.innerHTML = 'Ошибка загрузки данных';
            }
            console.error('Ошибка загрузки топ-5:', error);
        }
    },

    loadMatrix: async function() {
        const symbolInput = document.getElementById('symbol');
        if (!symbolInput) return;

        const rawSymbol = symbolInput.value.trim();
        if (!rawSymbol) return;

        const symbol = normalizeSymbol(rawSymbol);
        this.currentSymbol = symbol;

        try {
            const response = await fetch(`${this.API_BASE}/matrix/${symbol}`);
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }
            const data = await response.json();

            const matrixElement = document.getElementById('matrix');
            if (!matrixElement) return;

            if (!data.matrix || Object.keys(data.matrix).length === 0) {
                matrixElement.innerHTML = '<div class="status-message error">Данные для символа не найдены</div>';
                return;
            }

            let html = `<h3>Матрица для ${symbol}</h3><table class="excel-table"><thead><tr><th>Купить \\ Продать</th>`;

            data.sellExchanges.forEach(exchange => {
                html += `<th>${exchange}</th>`;
            });
            html += '</tr></thead><tbody>';

            data.buyExchanges.forEach(buyExchange => {
                html += `<tr><th>${buyExchange}</th>`;
                data.sellExchanges.forEach(sellExchange => {
                    const value = data.matrix[buyExchange][sellExchange];
                    let className = '';
                    if (value > 0) className = 'positive';
                    else if (value < 0) className = 'negative';
                    else className = 'zero';

                    html += `<td class="${className}">${value.toFixed(3)}%</td>`;
                });
                html += '</tr>';
            });
            html += '</tbody></table>';

            matrixElement.innerHTML = html;

        } catch (error) {
            const matrixElement = document.getElementById('matrix');
            if (matrixElement) {
                matrixElement.innerHTML = '<div class="status-message error">Ошибка загрузки матрицы</div>';
            }
            console.error('Ошибка загрузки матрицы:', error);
        }
    },

    startAutoUpdate: function() {
        this.loadTop5();
        if (this.currentSymbol && document.getElementById('symbol') && document.getElementById('symbol').value.trim()) {
            this.loadMatrix();
        }
        this.updateLastUpdateTime();
    },

    init: function() {
        if (window.location.pathname.includes('arbitrage') || window.location.pathname.includes('online')) {
            console.log('Инициализация арбитража');

            this.startAutoUpdate();
            const self = this;
            setInterval(function() {
                self.startAutoUpdate();
            }, 1000);

            const symbolInput = document.getElementById('symbol');
            if (symbolInput) {
                symbolInput.addEventListener('keypress', function(e) {
                    if (e.key === 'Enter') {
                        self.loadMatrix();
                    }
                });
            }
        }
    }
};

function loadMatrix() {
    ArbitrageModule.loadMatrix();
}

document.addEventListener('DOMContentLoaded', function() {
    setupWebSocketConnection();
    setupBrowserCloseHandler();

    ArbitrageModule.init();

    console.log('Страница загружена:', window.location.pathname);

    const checkSymbolBtn = document.getElementById('check-symbol-btn');
    if (checkSymbolBtn) {
        checkSymbolBtn.addEventListener('click', async function() {
            const exchange = document.getElementById('main-exchange').value;
            const symbol = document.getElementById('main-symbol').value.trim();
            const statusDiv = document.getElementById('status');

            if (!symbol) {
                statusDiv.className = 'status-message error';
                statusDiv.textContent = 'Введите символ!';
                return;
            }

            statusDiv.className = 'status-message processing';
            statusDiv.textContent = 'Проверка символа...';

            try {
                const response = await fetch('/api/symbolcheck', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        exchange: exchange,
                        symbol: symbol
                    })
                });

                const result = await response.json();

                if (response.ok && result.ok) {
                    statusDiv.className = 'status-message success';
                    statusDiv.textContent = 'Символ найден!';
                } else {
                    statusDiv.className = 'status-message error';
                    statusDiv.textContent = 'Символ не найден!';
                }
            } catch (error) {
                console.error('Ошибка:', error);
                statusDiv.className = 'status-message error';
                statusDiv.textContent = 'Ошибка проверки символа!';
            }
        });
    }

    const addColBtn = document.getElementById('add-col-btn');
    if (addColBtn) {
        addColBtn.addEventListener('click', function() {
            const paramsRow = document.getElementById('params-row');
            const exchangesRow = document.getElementById('exchanges-row');

            const newParamCell = document.createElement('td');
            newParamCell.innerHTML = `
                <select class="param-select">
                    <option value="open">Цена открытия</option>
                    <option value="close">Цена закрытия</option>
                    <option value="high">Максимум</option>
                    <option value="low">Минимум</option>
                </select>
            `;
            paramsRow.insertBefore(newParamCell, paramsRow.lastElementChild);

            const newExchangeCell = document.createElement('td');
            newExchangeCell.innerHTML = `
                <select class="exchange-select">
                    <option value="bybit">Bybit</option>
                    <option value="kucoin">Kucoin</option>
                    <option value="bitget">Bitget</option>
                    <option value="OKX">OKX</option>
                </select>
            `;
            exchangesRow.appendChild(newExchangeCell);
        });
    }

    const downloadBtn = document.getElementById('download-btn');
    if (downloadBtn) {
        downloadBtn.addEventListener('click', function() {
            const statusDiv = document.getElementById('status');
            statusDiv.textContent = 'Обработка запроса...';
            statusDiv.className = 'status-message processing';

            const rawSymbol = document.getElementById('main-symbol').value.trim();
            const symbol = rawSymbol;
            const startTime = document.getElementById('startTime').value;
            const endTime = document.getElementById('endTime').value;
            const interval = document.getElementById('interval').value;

            console.log('Проверка полей:', {
                symbol: symbol,
                startTime: startTime,
                endTime: endTime,
                interval: interval
            });

            if (!symbol || !startTime || !endTime) {
                statusDiv.textContent = 'Заполните все обязательные поля!';
                statusDiv.className = 'status-message error';
                return;
            }

            const start = new Date(startTime);
            const end = new Date(endTime);

            if (start >= end) {
                statusDiv.textContent = 'Время начала должно быть меньше времени окончания!';
                statusDiv.className = 'status-message error';
                return;
            }

            const paramSelects = document.querySelectorAll('.param-select');
            const exchangeSelects = document.querySelectorAll('.exchange-select');

            const columns = [];

            for (let i = 0; i < paramSelects.length; i++) {
                const columnData = {
                    type: paramSelects[i].value,
                    exchange: exchangeSelects[i] ? exchangeSelects[i].value : 'bybit'
                };
                columns.push(columnData);
            }

            const data = {
                symbol: symbol,
                startTime: startTime,
                endTime: endTime,
                interval: interval,
                columns: columns
            };

            console.log('Данные для скачивания:', data);

            fetch('/api/exportfile', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                console.log('Статус ответа:', response.status);
                console.log('Заголовки ответа:', response.headers);

                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`HTTP ${response.status}: ${text}`);
                    });
                }
                return response.blob();
            })
            .then(blob => {
                console.log('Получен blob размером:', blob.size, 'байт');
                console.log('Тип blob:', blob.type);

                if (blob.size === 0) {
                    throw new Error('Получен пустой файл');
                }

                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = `exchange_data_${symbol.replace(/\s+/g, '')}_${interval}_${new Date().toISOString().slice(0,10)}.xlsx`;
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);

                statusDiv.textContent = 'Файл успешно скачан!';
                statusDiv.className = 'status-message success';
            })
            .catch(error => {
                console.error('Ошибка при скачивании:', error);
                statusDiv.textContent = 'Ошибка при скачивании: ' + error.message;
                statusDiv.className = 'status-message error';
            });
        });
    }
});