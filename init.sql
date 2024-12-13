-- Создаем таблицу пользователей
CREATE TABLE users
(
    user_id       SERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    full_name     VARCHAR(100)        NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    role          VARCHAR(20)         NOT NULL CHECK (role IN ('administrator', 'project_manager', 'researcher'))
);

CREATE TABLE IF NOT EXISTS roles
(
    role_id SERIAL PRIMARY KEY,
    name    VARCHAR(20) NOT NULL
);

ALTER TABLE users
    DROP COLUMN role;
ALTER TABLE users
    ADD COLUMN role_id INTEGER REFERENCES roles (role_id) ON DELETE SET NULL;


-- Создаем таблицу проектов
CREATE TABLE projects
(
    project_id   SERIAL PRIMARY KEY,
    project_name VARCHAR(200) NOT NULL,
    description  TEXT,
    start_date   DATE         NOT NULL,
    end_date     DATE,
    status       VARCHAR(20)  NOT NULL DEFAULT 'planned' CHECK (status IN ('planned', 'ongoing', 'completed', 'on_hold')),
    manager_id   INTEGER      NOT NULL REFERENCES users (user_id) ON DELETE SET NULL
);

-- Создаем таблицу команд проекта (связь многие-ко-многим между пользователями и проектами)
CREATE TABLE project_team
(
    project_id      INTEGER NOT NULL REFERENCES projects (project_id) ON DELETE CASCADE,
    user_id         INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    role_in_project VARCHAR(50),
    PRIMARY KEY (project_id, user_id)
);

-- Создаем таблицу задач
CREATE TABLE tasks
(
    task_id     SERIAL PRIMARY KEY,
    project_id  INTEGER      NOT NULL REFERENCES projects (project_id) ON DELETE CASCADE,
    assigned_to INTEGER      REFERENCES users (user_id) ON DELETE SET NULL,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    due_date    DATE,
    status      VARCHAR(20)  NOT NULL DEFAULT 'not_started' CHECK (status IN ('not_started', 'in_progress', 'completed', 'on_hold'))
);

-- Создаем таблицу публикаций
CREATE TABLE publications
(
    publication_id   SERIAL PRIMARY KEY,
    project_id       INTEGER      REFERENCES projects (project_id) ON DELETE SET NULL,
    title            VARCHAR(300) NOT NULL,
    abstract         TEXT,
    publication_date DATE,
    link             VARCHAR(200)
);

-- Создаем таблицу авторов публикаций (связь многие-ко-многим между публикациями и пользователями)
CREATE TABLE publication_authors
(
    publication_id INTEGER NOT NULL REFERENCES publications (publication_id) ON DELETE CASCADE,
    user_id        INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (publication_id, user_id)
);

-- Создаем таблицу оборудования
CREATE TABLE equipment
(
    equipment_id        SERIAL PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    description         TEXT,
    location            VARCHAR(100),
    availability_status VARCHAR(20)  NOT NULL DEFAULT 'available' CHECK (availability_status IN ('available', 'in_use', 'under_maintenance'))
);

CREATE TABLE IF NOT EXISTS backups
(
    id          SERIAL PRIMARY KEY ,
    file_name  text,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP
);
-- Создаем таблицу бронирования оборудования
CREATE TABLE equipment_reservations
(
    reservation_id SERIAL PRIMARY KEY,
    equipment_id   INTEGER   NOT NULL REFERENCES equipment (equipment_id) ON DELETE CASCADE,
    user_id        INTEGER   NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    start_time     TIMESTAMP NOT NULL,
    end_time       TIMESTAMP NOT NULL,
    CONSTRAINT reservation_time_check CHECK (start_time < end_time)
);

-- Создаем таблицу источников финансирования
CREATE TABLE funding_sources
(
    funding_source_id SERIAL PRIMARY KEY,
    name              VARCHAR(200) NOT NULL,
    description       TEXT
);

-- Создаем таблицу финансирования проектов (связь многие-ко-многим между проектами и источниками финансирования)
CREATE TABLE project_funding
(
    project_id        INTEGER        NOT NULL REFERENCES projects (project_id) ON DELETE CASCADE,
    funding_source_id INTEGER        NOT NULL REFERENCES funding_sources (funding_source_id) ON DELETE CASCADE,
    amount            NUMERIC(15, 2) NOT NULL CHECK (amount > 0),
    PRIMARY KEY (project_id, funding_source_id)
);

-- Создаем таблицу бюджетов проектов
CREATE TABLE project_budgets
(
    budget_id        SERIAL PRIMARY KEY,
    project_id       INTEGER        NOT NULL REFERENCES projects (project_id) ON DELETE CASCADE,
    allocated_amount NUMERIC(15, 2) NOT NULL CHECK (allocated_amount >= 0),
    spent_amount     NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (spent_amount >= 0),
    CONSTRAINT budget_amount_check CHECK (spent_amount <= allocated_amount)
);

-- Устанавливаем расширение для криптографических функций
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Функция для хеширования пароля
CREATE OR REPLACE FUNCTION hash_password()
    RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'INSERT' OR (TG_OP = 'UPDATE' AND NEW.password_hash <> OLD.password_hash) THEN
        NEW.password_hash := crypt(NEW.password_hash, gen_salt('bf'));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
-- Создаем процедуру для регистрации пользователя без выходного параметра
CREATE OR REPLACE PROCEDURE register_user(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_full_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_role VARCHAR(100)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF EXISTS (SELECT 1 FROM users WHERE username = p_username OR email = p_email) THEN
        RAISE EXCEPTION 'Пользователь с таким именем или email уже существует';
    END IF;

    INSERT INTO users (username, password_hash, full_name, email, role)
    VALUES (p_username, crypt(p_password, gen_salt('bf')), p_full_name, p_email, p_role);
END;
$$;
-- Триггер, вызывающий функцию hash_password при вставке или обновлении пользователя
CREATE TRIGGER trg_hash_password
    BEFORE INSERT OR UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE hash_password();

-- Функция для проверки правильности введенного пароля
-- Функция для хеширования пароля
CREATE OR REPLACE FUNCTION hash_password()
    RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'INSERT' OR (TG_OP = 'UPDATE' AND NEW.password_hash <> OLD.password_hash) THEN
        NEW.password_hash := crypt(NEW.password_hash, gen_salt('bf'));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер для вызова функции хеширования при вставке или обновлении
CREATE TRIGGER hash_password_trigger
    BEFORE INSERT OR UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION hash_password();

-- Обновленная процедура для регистрации пользователя
CREATE OR REPLACE PROCEDURE register_user(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_full_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_role VARCHAR(100)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF EXISTS (SELECT 1 FROM users WHERE username = p_username OR email = p_email) THEN
        RAISE EXCEPTION 'Пользователь с таким именем или email уже существует';
    END IF;

    -- Вставляем пароль без хеширования; триггер выполнит хеширование автоматически
    INSERT INTO users (username, password_hash, full_name, email, role)
    VALUES (p_username, p_password, p_full_name, p_email, p_role);
END;
$$;

-- Функция аутентификации пользователя
CREATE OR REPLACE FUNCTION authenticate_user(p_username VARCHAR, p_password VARCHAR)
    RETURNS BOOLEAN AS
$$
DECLARE
    stored_hash VARCHAR(255);
BEGIN
    SELECT password_hash INTO stored_hash FROM users WHERE username = p_username;
    IF stored_hash IS NULL THEN
        RETURN FALSE;
    END IF;

    -- Сравнение пароля с использованием crypt и хешированного значения
    RETURN stored_hash = crypt(p_password, stored_hash);
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;
-- Функция для обновления статуса проекта на 'completed', если все задачи выполнены
CREATE OR REPLACE FUNCTION update_project_status()
    RETURNS TRIGGER AS
$$
DECLARE
    incomplete_tasks INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO incomplete_tasks
    FROM tasks
    WHERE project_id = NEW.project_id
      AND status <> 'completed';

    IF incomplete_tasks = 0 THEN
        UPDATE projects SET status = 'completed' WHERE project_id = NEW.project_id;
    ELSE
        UPDATE projects SET status = 'ongoing' WHERE project_id = NEW.project_id AND status = 'planned';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер, вызывающий функцию update_project_status после обновления задачи
CREATE TRIGGER trg_update_project_status
    AFTER INSERT OR UPDATE
    ON tasks
    FOR EACH ROW
EXECUTE PROCEDURE update_project_status();

-- Функция для проверки, что потраченная сумма не превышает выделенную
CREATE OR REPLACE FUNCTION check_budget_limits()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.spent_amount > NEW.allocated_amount THEN
        RAISE EXCEPTION 'Spent amount (%.2f) exceeds allocated budget (%.2f)', NEW.spent_amount, NEW.allocated_amount;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер, вызывающий функцию check_budget_limits при вставке или обновлении бюджета
CREATE TRIGGER trg_check_budget_limits
    BEFORE INSERT OR UPDATE
    ON project_budgets
    FOR EACH ROW
EXECUTE PROCEDURE check_budget_limits();

-- Представление для отображения активных (текущих) проектов и их менеджеров
CREATE OR REPLACE VIEW active_projects_view AS
SELECT p.project_id,
       p.project_name,
       p.status,
       u.full_name AS manager_name,
       p.start_date,
       p.end_date
FROM projects p
         INNER JOIN
     users u ON p.manager_id = u.user_id
WHERE p.status = 'ongoing';

-- Представление для отображения количества задач, назначенных каждому исследователю
CREATE OR REPLACE VIEW researcher_load_view AS
SELECT u.user_id,
       u.full_name,
       COUNT(t.task_id) AS assigned_tasks
FROM users u
         LEFT JOIN
     tasks t ON u.user_id = t.assigned_to
WHERE u.role = 'researcher'
GROUP BY u.user_id,
         u.full_name;

-- Представление для отображения бюджета проектов
CREATE OR REPLACE VIEW project_budget_view AS
SELECT p.project_id,
       p.project_name,
       pb.allocated_amount,
       pb.spent_amount,
       (pb.allocated_amount - pb.spent_amount) AS remaining_amount
FROM projects p
         INNER JOIN
     project_budgets pb ON p.project_id = pb.project_id;

-- Создаем индекс на таблицу tasks для ускорения выборки по assigned_to
CREATE INDEX idx_tasks_assigned_to ON tasks (assigned_to);

-- Создаем индекс на таблицу equipment_reservations для ускорения выборки по equipment_id
CREATE INDEX idx_equipment_reservations_equipment_id ON equipment_reservations (equipment_id);

-- Функция для проверки пересечения времени бронирования
CREATE OR REPLACE FUNCTION check_equipment_availability()
    RETURNS TRIGGER AS
$$
BEGIN
    IF EXISTS (SELECT 1
               FROM equipment_reservations
               WHERE equipment_id = NEW.equipment_id
                 AND reservation_id <> NEW.reservation_id
                 AND (
                   (NEW.start_time, NEW.end_time) OVERLAPS (start_time, end_time)
                   )) THEN
        RAISE EXCEPTION 'Equipment is already reserved for the specified time period.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер для проверки пересечения бронирований
CREATE TRIGGER trg_check_equipment_availability
    BEFORE INSERT OR UPDATE
    ON equipment_reservations
    FOR EACH ROW
EXECUTE PROCEDURE check_equipment_availability();
