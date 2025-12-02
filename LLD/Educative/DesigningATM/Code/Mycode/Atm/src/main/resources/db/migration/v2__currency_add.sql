-- 1. First add the column as nullable
ALTER TABLE account ADD COLUMN currency_code VARCHAR(3);

-- 2. Set default value for existing rows
UPDATE account SET currency_code = 'USD' WHERE currency_code IS NULL;

-- 3. Now make it NOT NULL
ALTER TABLE account ALTER COLUMN currency_code SET NOT NULL;