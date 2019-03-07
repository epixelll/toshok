CREATE INDEX idx_giftd_account_id
ON gifts(account_id);

CREATE INDEX idx_accounts
ON accounts(id, status, fullname, region_id, "level");