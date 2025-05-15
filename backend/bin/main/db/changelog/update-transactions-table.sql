--liquibase formatted sql

--changeset beaverbyte:1

-- Populating the new entities with existing data, like account or category
INSERT INTO accounts (name)
SELECT DISTINCT account FROM transactions WHERE account IS NOT NULL;

INSERT INTO categories (name)
SELECT DISTINCT category FROM transactions WHERE category IS NOT NULL;
INSERT INTO merchants (name)
SELECT DISTINCT merchant FROM transactions WHERE merchant IS NOT NULL;


-- Updating transactions table with new foreign keys

-- Update account_id
UPDATE transactions
SET account_id = matched_account.id
FROM accounts AS matched_account
WHERE transactions.account = matched_account.name
  AND transactions.account IS NOT NULL;

-- Update category_id
UPDATE transactions
SET category_id = matched_category.id
FROM categories AS matched_category
WHERE transactions.category = matched_category.name
  AND transactions.category IS NOT NULL;

-- Update merchant_id
UPDATE transactions
SET merchant_id = matched_merchant.id
FROM merchants AS matched_merchant
WHERE transactions.merchant = matched_merchant.name
  AND transactions.merchant IS NOT NULL;


--rollback
UPDATE transactions 
SET account_id = NULL, category_id = NULL, merchant_id = NULL;

DELETE FROM merchants;
DELETE FROM categories;
DELETE FROM accounts;