    INSERT INTO
        policy
        (created_at, external_id, start_date)
    VALUES
        (1669632320298, 'CU423DF89', '2022-11-29');

    INSERT INTO
        insured_person
        (first_name, second_name, premium)
    VALUES
        ('Jane', 'Johnson', 12.90),
        ('Jack', 'Doe', 15.90),
        ('Tim', 'Button', 10);

    INSERT INTO
        policy_insured_person
        (policy_internal_id, person_id)
    values
        (1, 1),
        (1, 2),
        (1, 3);

