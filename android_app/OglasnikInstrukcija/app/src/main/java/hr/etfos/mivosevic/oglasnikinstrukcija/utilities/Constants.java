package hr.etfos.mivosevic.oglasnikinstrukcija.utilities;

/**
 * Created by admin on 18.6.2016..
 */
public class Constants {
    public static final String SERVER_ADDRESS = "http://10.0.3.2/instruction_tracker/";
    public static final String SERVER_UPLOAD_DIR = "Uploads/";
    public static final String CHECK_USER_SCRIPT = "check_user.php";
    public static final String INSERT_USER_SCRIPT = "insert_user.php";
    public static final String FILE_UPLOAD_SCRIPT = "file_upload.php";
    public static final String GET_USER_SUBJECTS_SCRIPT = "get_user_subjects.php";
    public static final String INSERT_SUBJECT_SCRIPT = "insert_subject.php";

    public static final String USERNAME_DB_TAG = "username";
    public static final String PASSWORD_DB_TAG = "passw";
    public static final String NAME_DB_TAG = "name";
    public static final String EMAIL_DB_TAG = "email";
    public static final String PHONE_DB_TAG = "phone_num";
    public static final String LOCATION_DB_TAG = "location";
    public static final String ABOUT_DB_TAG = "about";
    public static final String IMAGEURL_DB_TAG = "img_url";

    public static final String SUBJECT_ID_DB_TAG = "subject_id";
    public static final String SUBJECT_NAME_DB_TAG = "subject_name";
    public static final String SUBJECT_TAGS_DB_TAG = "subject_tags";

    public static final String USER_TAG = "user";

    public static final String USERNAME_REGEX = "^(?=.{6,64}$)(?![.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![.])$";
    public static final String PASSWORD_REGEX = "^.*(?=.{6,})(?=.*[a-zA-Z\\d!#$%&?]).*$";
    public static final String EMAIL_REGEX = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

    public static final String USERNAME_NOT_VALID = "Korisničko ime mora imati barem 6 znakova.\nSadrži slova i brojke, te znakove \"_\" i \".\".\nNe može počinjati ni završavati točkom.";
    public static final String PASSWORD_NOT_VALID = "Lozinka mora imati barem 6 znakova.\nSadrži slova, brojke i znakove: !#$%&?";
    public static final String EMAIL_NOT_VALID = "Pogrešan format adrese e-pošte.";
    public static final String PHONE_NOT_VALID = "Pogrešno unesen broj telefona.";
    public static final String ADDRESS_NOT_VALID = "Pogrešno unesena adresa.";
    public static final String PASSWORD_NOT_MATCH = "Unesene lozinke se ne podudaraju.";

    public static final String USER_PREFS_FILE = "userPrefs";

    public static final int SELECT_IMAGE_CODE = 123;

    public static final String SUBJECT_NAME_INVALID = "Nije uspjelo: Pogršeno upisano ime predmeta";
    public static final String SUBJECT_TAGS_INVALID = "Nije uspjelo: Pogrešno navedene oznake\nPotrebno koristiti mala slova i odvajati zarezom";

    public static final String NEW_SUBJECT_DIALOG_TAG = "NewSubjectDialog";
}
