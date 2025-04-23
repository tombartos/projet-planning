CREATE USER llaurent704 WITH PASSWORD 'password';
GRANT CONNECT ON DATABASE postgres TO llaurent704;
GRANT USAGE ON SCHEMA public TO llaurent704;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO llaurent704;
GRANT USAGE, SELECT ON SEQUENCE public."note_perso_sequence" TO llaurent704;
GRANT INSERT ON TABLE notes_perso TO llaurent704;
GRANT UPDATE ON TABLE notes_perso TO llaurent704;

CREATE USER tbaron132 WITH PASSWORD 'password';
GRANT CONNECT ON DATABASE postgres TO tbaron132;
GRANT USAGE ON SCHEMA public TO tbaron132;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO tbaron132;
GRANT INSERT ON ALL TABLES IN SCHEMA public TO tbaron132;
GRANT UPDATE ON ALL TABLES IN SCHEMA public TO tbaron132;
GRANT DELETE ON ALL TABLES IN SCHEMA public TO tbaron132;
GRANT USAGE, SELECT ON SEQUENCE public."note_perso_sequence" TO tbaron132;
GRANT INSERT ON TABLE notes_perso TO tbaron132;
GRANT UPDATE ON TABLE notes_perso TO tbaron132;
GRANT USAGE, SELECT ON SEQUENCE public."demande_creneau_sequence" TO tbaron132;
GRANT INSERT ON TABLE demandes_creneaux TO tbaron132;
GRANT UPDATE ON TABLE demandes_creneaux TO tbaron132;

CREATE USER dupont888 WITH PASSWORD 'password';
GRANT CONNECT ON DATABASE postgres TO dupont888;
GRANT USAGE ON SCHEMA public TO dupont888;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO dupont888;
GRANT INSERT ON ALL TABLES IN SCHEMA public TO dupont888;
GRANT UPDATE ON ALL TABLES IN SCHEMA public TO dupont888;
GRANT DELETE ON ALL TABLES IN SCHEMA public TO dupont888;
GRANT USAGE, SELECT ON SEQUENCE public."demande_creneau_sequence" TO dupont888;
GRANT INSERT ON TABLE demandes_creneaux TO dupont888;
GRANT UPDATE ON TABLE demandes_creneaux TO dupont888;

DO $$
BEGIN
    EXECUTE (
        SELECT string_agg(
            format('GRANT USAGE, SELECT, UPDATE ON SEQUENCE %I.%I TO dupont888;', schemaname, sequencename),
            ' '
        )
        FROM pg_sequences
        WHERE schemaname NOT IN ('pg_catalog', 'information_schema')
    );
END $$;