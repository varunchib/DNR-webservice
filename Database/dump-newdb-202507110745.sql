PGDMP  &    -                }            newdb    17.0    17.0     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16387    newdb    DATABASE     �   CREATE DATABASE newdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE newdb;
                     postgres    false                        2615    166625    dnrcore    SCHEMA        CREATE SCHEMA dnrcore;
    DROP SCHEMA dnrcore;
                     postgres    false            >           1255    183226   prr_create_quotation(character varying, character varying, date, date, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, character varying, text, text, uuid, character varying) 	   PROCEDURE     �  CREATE PROCEDURE dnrcore.prr_create_quotation(IN p_i_flag character varying, IN p_i_reference_no character varying, IN p_i_date date, IN p_i_expiration_at date, IN p_i_company_name character varying, IN p_i_attention character varying, IN p_i_designation character varying, IN p_i_email character varying, IN p_i_phone character varying, IN p_i_address character varying, IN p_i_website character varying, IN p_i_subject character varying, IN p_i_project character varying, IN p_i_columns text, IN p_i_rows text, IN p_i_created_by uuid, IN p_i_author_name character varying, OUT p_json_result jsonb)
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_quotation_id UUID := gen_random_uuid();
    v_column JSONB;
    v_row JSONB;
    v_row_id UUID;
    v_cell_key TEXT;
    v_cell_value TEXT;
    v_result_status VARCHAR := 'S';
    v_result_msg TEXT := 'Quotation created successfully';
    v_doc_version TEXT := 'ORIGINAL';
    v_version_count INTEGER;
BEGIN
    IF p_i_flag = 'N' THEN
    -- ✅ Fix: NULL-safe comparison using IS NOT DISTINCT FROM
    IF EXISTS (
        SELECT 1 FROM dnrcore.quotations
        WHERE reference_no = p_i_reference_no
          AND created_by IS NOT DISTINCT FROM p_i_created_by
          AND doc_version = 'ORIGINAL'
    ) THEN
        p_json_result := jsonb_build_object(
            'resultStatus', 'F',
            'resultMessage', 'Duplicate quotation: ORIGINAL version already exists for this reference number and user',
            'resultContent', NULL
        );
        RETURN;
    END IF;


    ELSIF p_i_flag = 'E' THEN
    -- 🛑 Ensure ORIGINAL exists before allowing revision
    IF NOT EXISTS (
        SELECT 1 FROM dnrcore.quotations
        WHERE reference_no = p_i_reference_no
          AND created_by IS NOT DISTINCT FROM p_i_created_by
          AND doc_version = 'ORIGINAL'
    ) THEN
        p_json_result := jsonb_build_object(
            'resultStatus', 'F',
            'resultMessage', 'Cannot create revision: ORIGINAL quotation does not exist for the given reference number and user.',
            'resultContent', NULL
        );
        RETURN;
    END IF;

    -- Count existing versions to determine next REV number
    SELECT MAX(CAST(substring(doc_version FROM 4) AS INTEGER))
    INTO v_version_count
    FROM dnrcore.quotations
    WHERE reference_no = p_i_reference_no
      AND created_by IS NOT DISTINCT FROM p_i_created_by
      AND doc_version LIKE 'REV%';

    IF v_version_count IS NULL THEN
        v_version_count := 0;
    END IF;

    v_doc_version := 'REV' || LPAD((v_version_count + 1)::TEXT, 2, '0');

    END IF;

    -- Insert into quotations
    INSERT INTO dnrcore.quotations (
        id, reference_no, date, expiration_at, company_name, attention, designation,
        email, phone, address, website, subject, project, created_by, author_name, doc_version
    )
    VALUES (
        v_quotation_id, p_i_reference_no, p_i_date, p_i_expiration_at, p_i_company_name, p_i_attention,
        p_i_designation, p_i_email, p_i_phone, p_i_address, p_i_website,
        p_i_subject, p_i_project, p_i_created_by, p_i_author_name, v_doc_version
    );

    -- Insert columns
    FOR v_column IN SELECT * FROM jsonb_array_elements(p_i_columns::jsonb)
    LOOP
        INSERT INTO dnrcore.quotation_columns (
            id, column_id, column_name, visible, width, quotation_id
        )
        VALUES (
            gen_random_uuid(),
            v_column->>'columnId',
            v_column->>'columnName',
            COALESCE((v_column->>'visible')::BOOLEAN, TRUE),
            COALESCE((v_column->>'width')::INTEGER, 150),
            v_quotation_id
        );
    END LOOP;

    -- Insert rows and cells
    FOR v_row IN SELECT * FROM jsonb_array_elements(p_i_rows::jsonb)
    LOOP
        v_row_id := gen_random_uuid();

        INSERT INTO dnrcore.quotation_rows (
            id, row_index, quotation_id
        ) VALUES (
            v_row_id,
            (v_row->>'rowIndex')::INTEGER,
            v_quotation_id
        );

        FOR v_cell_key, v_cell_value IN
            SELECT key, value FROM jsonb_each_text(v_row->'cells')
        LOOP
            INSERT INTO dnrcore.quotation_cells (
                id, column_id, "value", row_id
            ) VALUES (
                gen_random_uuid(),
                v_cell_key,
                v_cell_value,
                v_row_id
            );
        END LOOP;
    END LOOP;

    -- Final result
    p_json_result := jsonb_build_object(
        'resultStatus', v_result_status,
        'resultMessage', v_result_msg,
        'resultContent', jsonb_build_object('quotationId', v_quotation_id, 'docVersion', v_doc_version)
    );

EXCEPTION
    WHEN OTHERS THEN
        p_json_result := jsonb_build_object(
            'resultStatus', 'F',
            'resultMessage', 'Stored Procedure Error: ' || SQLERRM,
            'resultContent', NULL
        );
END;
$$;
 W  DROP PROCEDURE dnrcore.prr_create_quotation(IN p_i_flag character varying, IN p_i_reference_no character varying, IN p_i_date date, IN p_i_expiration_at date, IN p_i_company_name character varying, IN p_i_attention character varying, IN p_i_designation character varying, IN p_i_email character varying, IN p_i_phone character varying, IN p_i_address character varying, IN p_i_website character varying, IN p_i_subject character varying, IN p_i_project character varying, IN p_i_columns text, IN p_i_rows text, IN p_i_created_by uuid, IN p_i_author_name character varying, OUT p_json_result jsonb);
       dnrcore               postgres    false    7            =           1255    183137 s   prr_get_paginated_quotations(integer, integer, character varying, character varying, uuid, character varying, uuid) 	   PROCEDURE     *  CREATE PROCEDURE dnrcore.prr_get_paginated_quotations(OUT p_json_result jsonb, IN p_i_page integer DEFAULT 0, IN p_i_size integer DEFAULT 5, IN p_i_reference_no character varying DEFAULT NULL::character varying, IN p_i_status character varying DEFAULT NULL::character varying, IN p_i_created_by uuid DEFAULT NULL::uuid, IN p_i_author_name character varying DEFAULT NULL::character varying, IN p_i_quotation_id uuid DEFAULT NULL::uuid)
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_result_status VARCHAR(1) := 'S';
    v_result_msg TEXT := '';
    v_json_data JSONB := '[]';
    v_total_count INT := 0;
BEGIN
    ----------------------------------------------------------------------
    -- CASE 1: Fetch full data for one quotation by ID (if provided)
    ----------------------------------------------------------------------
    IF p_i_quotation_id IS NOT NULL THEN
        SELECT jsonb_build_object(
            'quotation', (
                SELECT to_jsonb(q) FROM dnrcore.quotations q WHERE q.id = p_i_quotation_id
            ),
            'columns', (
                SELECT COALESCE(jsonb_agg(to_jsonb(c)), '[]')
                FROM dnrcore.quotation_columns c
                WHERE c.quotation_id = p_i_quotation_id
            ),
            'rows', (
                SELECT COALESCE(jsonb_agg(to_jsonb(r)), '[]')
                FROM dnrcore.quotation_rows r
                WHERE r.quotation_id = p_i_quotation_id
            ),
            'cells', (
                SELECT COALESCE(jsonb_agg(to_jsonb(cell)), '[]')
                FROM dnrcore.quotation_cells cell
                WHERE cell.row_id IN (
                    SELECT r.id FROM dnrcore.quotation_rows r WHERE r.quotation_id = p_i_quotation_id
                )
            )
        ) INTO v_json_data;

        IF v_json_data IS NULL OR v_json_data->'quotation' IS NULL THEN
            v_result_status := 'F';
            v_result_msg := 'Quotation not found.';
            v_json_data := NULL;
        ELSE
            v_result_msg := 'Quotation fetched successfully.';
        END IF;

        p_json_result := jsonb_build_object(
            'resultStatus', v_result_status,
            'resultCode', CASE WHEN v_result_status = 'S' THEN '200' ELSE '404' END,
            'resultMessage', v_result_msg,
            'resultContent', v_json_data
        );
        RETURN;
    END IF;

    ----------------------------------------------------------------------
    -- CASE 2: Paginated list of quotations (summary)
    ----------------------------------------------------------------------

    SELECT COUNT(*) INTO v_total_count
    FROM dnrcore.quotations q
    WHERE (p_i_reference_no IS NULL OR q.reference_no ILIKE '%' || p_i_reference_no || '%')
      AND (p_i_status IS NULL OR q.status = p_i_status)
      AND (p_i_created_by IS NULL OR q.created_by = p_i_created_by)
      AND (p_i_author_name IS NULL OR q.author_name ILIKE '%' || p_i_author_name || '%');

    SELECT jsonb_agg(to_jsonb(q)) INTO v_json_data
    FROM (
        SELECT *
        FROM dnrcore.quotations q
        WHERE (p_i_reference_no IS NULL OR q.reference_no ILIKE '%' || p_i_reference_no || '%')
          AND (p_i_status IS NULL OR q.status = p_i_status)
          AND (p_i_created_by IS NULL OR q.created_by = p_i_created_by)
          AND (p_i_author_name IS NULL OR q.author_name ILIKE '%' || p_i_author_name || '%')
        ORDER BY q.created_at DESC
        OFFSET p_i_page * p_i_size
        LIMIT p_i_size
    ) q;

    IF v_json_data IS NULL THEN
        v_json_data := '[]';
        v_result_status := 'F';
        v_result_msg := 'No quotations found.';
    ELSE
        v_result_msg := 'Quotations fetched successfully.';
    END IF;

    p_json_result := jsonb_build_object(
        'resultStatus', v_result_status,
        'resultCode', CASE WHEN v_result_status = 'S' THEN '200' ELSE '404' END,
        'resultMessage', v_result_msg,
        'page', p_i_page,
        'size', p_i_size,
        'totalCount', v_total_count,
        'resultContent', v_json_data
    );

EXCEPTION
    WHEN OTHERS THEN
        p_json_result := jsonb_build_object(
            'resultStatus', 'F',
            'resultCode', '500',
            'resultMessage', 'ERROR: ' || SQLERRM,
            'resultContent', NULL
        );
END;
$$;
   DROP PROCEDURE dnrcore.prr_get_paginated_quotations(OUT p_json_result jsonb, IN p_i_page integer, IN p_i_size integer, IN p_i_reference_no character varying, IN p_i_status character varying, IN p_i_created_by uuid, IN p_i_author_name character varying, IN p_i_quotation_id uuid);
       dnrcore               postgres    false    7            
           1259    166628    quotation_cells    TABLE     �   CREATE TABLE dnrcore.quotation_cells (
    id uuid NOT NULL,
    column_id character varying(100),
    value text,
    row_id uuid NOT NULL
);
 $   DROP TABLE dnrcore.quotation_cells;
       dnrcore         heap r       postgres    false    7                       1259    166633    quotation_columns    TABLE     �   CREATE TABLE dnrcore.quotation_columns (
    id uuid NOT NULL,
    column_id character varying(100),
    column_name character varying(255),
    visible boolean NOT NULL,
    width integer NOT NULL,
    quotation_id uuid NOT NULL
);
 &   DROP TABLE dnrcore.quotation_columns;
       dnrcore         heap r       postgres    false    7                       1259    166636    quotation_rows    TABLE     ~   CREATE TABLE dnrcore.quotation_rows (
    id uuid NOT NULL,
    row_index integer NOT NULL,
    quotation_id uuid NOT NULL
);
 #   DROP TABLE dnrcore.quotation_rows;
       dnrcore         heap r       postgres    false    7                       1259    166639 
   quotations    TABLE     �  CREATE TABLE dnrcore.quotations (
    date date,
    created_at timestamp(6) without time zone DEFAULT now(),
    created_by uuid,
    id uuid NOT NULL,
    address character varying(255),
    attention character varying(255),
    company_name character varying(255),
    designation character varying(255),
    email character varying(255),
    pdf_url character varying(255),
    phone character varying(255),
    project character varying(255),
    reference_no character varying(255),
    status character varying(255),
    subject character varying(255),
    website character varying(255),
    expiration_at date,
    author_name character varying(255),
    doc_version character varying(50) DEFAULT 'ORIGINAL'::character varying
);
    DROP TABLE dnrcore.quotations;
       dnrcore         heap r       postgres    false    7                       1259    166644    users    TABLE     �   CREATE TABLE dnrcore.users (
    id uuid NOT NULL,
    email character varying(255),
    employee_id character varying(255),
    full_name character varying(255),
    password character varying(255),
    role character varying(255)
);
    DROP TABLE dnrcore.users;
       dnrcore         heap r       postgres    false    7                       1259    166649    uuid    SEQUENCE     o   CREATE SEQUENCE dnrcore.uuid
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    DROP SEQUENCE dnrcore.uuid;
       dnrcore               postgres    false    7            �          0    166628    quotation_cells 
   TABLE DATA           H   COPY dnrcore.quotation_cells (id, column_id, value, row_id) FROM stdin;
    dnrcore               postgres    false    266   @O       �          0    166633    quotation_columns 
   TABLE DATA           f   COPY dnrcore.quotation_columns (id, column_id, column_name, visible, width, quotation_id) FROM stdin;
    dnrcore               postgres    false    267    Q       �          0    166636    quotation_rows 
   TABLE DATA           F   COPY dnrcore.quotation_rows (id, row_index, quotation_id) FROM stdin;
    dnrcore               postgres    false    268   >R       �          0    166639 
   quotations 
   TABLE DATA           �   COPY dnrcore.quotations (date, created_at, created_by, id, address, attention, company_name, designation, email, pdf_url, phone, project, reference_no, status, subject, website, expiration_at, author_name, doc_version) FROM stdin;
    dnrcore               postgres    false    269   �R       �          0    166644    users 
   TABLE DATA           S   COPY dnrcore.users (id, email, employee_id, full_name, password, role) FROM stdin;
    dnrcore               postgres    false    270   8T       �           0    0    uuid    SEQUENCE SET     4   SELECT pg_catalog.setval('dnrcore.uuid', 1, false);
          dnrcore               postgres    false    271            *           2606    166651 $   quotation_cells quotation_cells_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY dnrcore.quotation_cells
    ADD CONSTRAINT quotation_cells_pkey PRIMARY KEY (id);
 O   ALTER TABLE ONLY dnrcore.quotation_cells DROP CONSTRAINT quotation_cells_pkey;
       dnrcore                 postgres    false    266            ,           2606    166653 (   quotation_columns quotation_columns_pkey 
   CONSTRAINT     g   ALTER TABLE ONLY dnrcore.quotation_columns
    ADD CONSTRAINT quotation_columns_pkey PRIMARY KEY (id);
 S   ALTER TABLE ONLY dnrcore.quotation_columns DROP CONSTRAINT quotation_columns_pkey;
       dnrcore                 postgres    false    267            .           2606    166655 "   quotation_rows quotation_rows_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY dnrcore.quotation_rows
    ADD CONSTRAINT quotation_rows_pkey PRIMARY KEY (id);
 M   ALTER TABLE ONLY dnrcore.quotation_rows DROP CONSTRAINT quotation_rows_pkey;
       dnrcore                 postgres    false    268            0           2606    166657    quotations quotations_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY dnrcore.quotations
    ADD CONSTRAINT quotations_pkey PRIMARY KEY (id);
 E   ALTER TABLE ONLY dnrcore.quotations DROP CONSTRAINT quotations_pkey;
       dnrcore                 postgres    false    269            3           2606    166738    users unique_employee_id 
   CONSTRAINT     [   ALTER TABLE ONLY dnrcore.users
    ADD CONSTRAINT unique_employee_id UNIQUE (employee_id);
 C   ALTER TABLE ONLY dnrcore.users DROP CONSTRAINT unique_employee_id;
       dnrcore                 postgres    false    270            5           2606    166659    users users_pkey 
   CONSTRAINT     O   ALTER TABLE ONLY dnrcore.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 ;   ALTER TABLE ONLY dnrcore.users DROP CONSTRAINT users_pkey;
       dnrcore                 postgres    false    270            1           1259    183225 !   uq_quotation_versioned_submission    INDEX     �   CREATE UNIQUE INDEX uq_quotation_versioned_submission ON dnrcore.quotations USING btree (reference_no, created_by, doc_version);
 6   DROP INDEX dnrcore.uq_quotation_versioned_submission;
       dnrcore                 postgres    false    269    269    269            6           2606    166720    quotation_cells fk_cells_row    FK CONSTRAINT     �   ALTER TABLE ONLY dnrcore.quotation_cells
    ADD CONSTRAINT fk_cells_row FOREIGN KEY (row_id) REFERENCES dnrcore.quotation_rows(id) ON DELETE CASCADE;
 G   ALTER TABLE ONLY dnrcore.quotation_cells DROP CONSTRAINT fk_cells_row;
       dnrcore               postgres    false    4910    268    266            8           2606    166715 &   quotation_columns fk_columns_quotation    FK CONSTRAINT     �   ALTER TABLE ONLY dnrcore.quotation_columns
    ADD CONSTRAINT fk_columns_quotation FOREIGN KEY (quotation_id) REFERENCES dnrcore.quotations(id) ON DELETE CASCADE;
 Q   ALTER TABLE ONLY dnrcore.quotation_columns DROP CONSTRAINT fk_columns_quotation;
       dnrcore               postgres    false    267    269    4912            9           2606    166710     quotation_rows fk_rows_quotation    FK CONSTRAINT     �   ALTER TABLE ONLY dnrcore.quotation_rows
    ADD CONSTRAINT fk_rows_quotation FOREIGN KEY (quotation_id) REFERENCES dnrcore.quotations(id) ON DELETE CASCADE;
 K   ALTER TABLE ONLY dnrcore.quotation_rows DROP CONSTRAINT fk_rows_quotation;
       dnrcore               postgres    false    268    269    4912            7           2606    166660 +   quotation_cells fkthmxdsb7pebj2070c1uysttcv    FK CONSTRAINT     �   ALTER TABLE ONLY dnrcore.quotation_cells
    ADD CONSTRAINT fkthmxdsb7pebj2070c1uysttcv FOREIGN KEY (row_id) REFERENCES dnrcore.quotation_rows(id);
 V   ALTER TABLE ONLY dnrcore.quotation_cells DROP CONSTRAINT fkthmxdsb7pebj2070c1uysttcv;
       dnrcore               postgres    false    266    4910    268            �   �  x���;n1E�UxD�#j	�� iH} ��د����p� �L3����E]��Al�I�7u��x^~���9��Zjk���2�lŀ�������ɼZٻ �p`�lC�;so��m��z~[��ߗ�^��Ab�+��<��y��1q�8^^�}�R�5h�^6��#&p�z��4�:.o�%�a!J
��.^6Z��v�}��ĥ��K���r��d�Gy/��������o~{��|�.MG��n���%<ڀe�Ww����Wp	��ҽ����k�ު(0��� F��Z��`Nz�a�8`�N]J0�8v�)HFY97�U@뚦8���<����QF�J��W�>�s���>{p	zw�I�BLY�ӣ
.M���Dj�Q�T��O�Vs\,�iD�3[F�;���>OE���V��������]*��I�fI�|d�{��s�O\���r��_Ȃ`      �     x���1NC1��Sp�8N�d� 0��ر#����1p{"f*��e�����4�2Ȕ&�h�"YuJ0����e?��)K��LX��Y c�-5�(KR�db�1��!+�5�;I�]=|?��[NǾ���ލ3�:�\ě����D����e�O�����>PǴ��h�r�
�p��9�T�����JX��_����hJ疕�pS�ސ+��?������1������.x�⹖e�KZK� JT:{�ACn�+��y۶_^n��      �   �   x���ˑ!�s�KmH��e.��&l�����r�c��PWh�����rF�/����C�Q�1O����H?-�V�8\dC}rQa˽�>��y�7Kz�/J,����F�)�(U(߮�)l�u	jL��5G�{!��0�i�wOy��´x�줷�7���Z��>L
      �   4  x���N�0E��WxN=v;YQD@Em)�Ī�B���J���;��ٍ�Ν�F� T��0�%�K����R)�HpDkݐ���h� �Ef�m��&G`f�CN�jɀYR8����g�ΙC�8��ƶ���q�>^���ݢ�p��]�hi;�w���C�!�i��#��^����dꂿ�3��С�]P(���3���wx՝O_E�У����3�<��7a�U�p���}8M����1��r����Gz���z���.�	�S�,)O%e���vZc8��F{J2�(��FR�%��)��
�!M�����      �   �   x�E�=�0@����-�vS���cL\��5a>�?�������^X;�HD	��$N](�����
x�z��<�/���(4�����$�˹Q>ĸ+�#W�aiRD�:�8뒷�2b�[�-�,��i��p\9�o���zj�3WJ}1�0M     