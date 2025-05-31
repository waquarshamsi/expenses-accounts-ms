CREATE TABLE account_type (
    account_type_id SERIAL PRIMARY KEY,
    account_type VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE accounts (
    account_number UUID PRIMARY KEY,
    account_name VARCHAR(20) NOT NULL,
    institution_name VARCHAR(50) NOT NULL,
    account_type_id INT NOT NULL REFERENCES account_type(account_type_id),
    account_status VARCHAR(10) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    description TEXT,
    user_id VARCHAR(50) NOT NULL
);

CREATE TABLE account_details (
    account_id UUID PRIMARY KEY REFERENCES accounts(account_number),
    interest_rate DECIMAL(10,4),
    credit_limit DECIMAL(19,2),
    loan_amount DECIMAL(19,2),
    maturity_date TIMESTAMP,
    investment_type VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Insert default account types
INSERT INTO account_type (account_type, description) VALUES 
    ('SAVINGS', 'Regular savings account'),
    ('CURRENT', 'Current or checking account'),
    ('CREDIT_CARD', 'Credit card account'),
    ('LOAN', 'Loan account'),
    ('INVESTMENT', 'Investment account'),
    ('DIGITAL_WALLET', 'Digital wallet');

-- Create indexes
CREATE INDEX idx_accounts_user_id ON accounts(user_id);
CREATE INDEX idx_accounts_account_type_id ON accounts(account_type_id);
CREATE INDEX idx_accounts_institution ON accounts(institution_name);
