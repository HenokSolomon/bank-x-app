
-- insert default system account
insert into account(account_id, account_type, account_number, principal, credential, created_by, created_date, updated_by , updated_date)
values ('6ddeaba3-e825-4e0c-ba1f-edc13c5e7f1c', 'SYSTEM', '000001', '', '', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

-- insert internal financial accounts owned by the system account
insert into financial_account (financial_account_id , financial_account_type , account_id , available_balance , created_by, created_date, updated_by , updated_date)
values ('eb68d11c-a455-490f-b5c9-956f577856fd', 'INTEREST_EXPENSE_ACCOUNT', '6ddeaba3-e825-4e0c-ba1f-edc13c5e7f1c', 0.0, 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into financial_account (financial_account_id , financial_account_type , account_id , available_balance , created_by, created_date, updated_by , updated_date)
values ('ca6dcfd2-8d03-4a50-ad6e-7f59d5c3b25a', 'BONUS_PAYMENT_EXPENSE_ACCOUNT', '6ddeaba3-e825-4e0c-ba1f-edc13c5e7f1c', 0.0, 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into financial_account (financial_account_id , financial_account_type , account_id , available_balance , created_by, created_date, updated_by , updated_date)
values ('36a1a96e-57c0-45c1-9d1a-fc8554749a4e', 'FEE_INCOME_ACCOUNT', '6ddeaba3-e825-4e0c-ba1f-edc13c5e7f1c', 0.0, 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into financial_account (financial_account_id , financial_account_type , account_id , available_balance , created_by, created_date, updated_by , updated_date)
values ('63c368bb-2277-469a-8604-9030093384aa', 'REVENUE_ACCOUNT', '6ddeaba3-e825-4e0c-ba1f-edc13c5e7f1c', 0.0, 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());


--create bankZ bank_institution account for testing purpose
insert into account(account_id, account_type, account_number, principal, credential, created_by, created_date, updated_by , updated_date)
values ('84685ff0-f8e8-420b-a7f4-a9f1396ec685', 'BUSINESS', '000002', '', '', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into bank_institution ( bank_institution_id , bank_name , account_id , created_by, created_date, updated_by , updated_date)
values('eac012cd-8090-45b6-93f6-cb42b71d0dd3', 'Bank Z', '84685ff0-f8e8-420b-a7f4-a9f1396ec685', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into financial_account (financial_account_id , financial_account_type , account_id , available_balance , created_by, created_date, updated_by , updated_date)
values ('577b5e1a-b859-4967-b6b3-8bf56b9fa3ee', 'THIRD_PARTY_BANK_PAYABLE', '84685ff0-f8e8-420b-a7f4-a9f1396ec685', 0.0, 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into financial_account (financial_account_id , financial_account_type , account_id , available_balance , created_by, created_date, updated_by , updated_date)
values ('5a240dfb-5438-4287-883f-8dd77e1cc03a', 'THIRD_PARTY_BANK_RECEIVABLE', '84685ff0-f8e8-420b-a7f4-a9f1396ec685', 0.0, 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());