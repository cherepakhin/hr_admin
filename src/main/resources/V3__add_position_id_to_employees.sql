-- Добавляем колонку position_id
ALTER TABLE employees
ADD COLUMN position_id BIGINT;

-- Устанавливаем значение по умолчанию (должность 'Сотрудник' — id=1)
UPDATE employees SET position_id = 1 WHERE position_id IS NULL;

-- Делаем колонку NOT NULL и добавляем внешний ключ
ALTER TABLE employees
MODIFY COLUMN position_id BIGINT NOT NULL,
ADD CONSTRAINT fk_employee_position
    FOREIGN KEY (position_id) REFERENCES positions(id);