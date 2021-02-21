package co.desofsi.cursosutc.data;

public class Constant {

    //public static  final String URL = "https://www.clinicasourilatacunga.com/";
    //public static final String URL = "http://192.168.100.8/";
    public static final String URL = "http://54.91.31.233/";
    public static final String URL_IMAGE = "http://192.168.0.102";
    public static final String HOME = URL + "api";
    public static final String LOGIN = HOME + "/login";
    public static final String LOGOUT = HOME + "/logout";
    public static final String REGISTER = HOME + "/register";
    public static final String PROFILE = HOME + "/profile";
    public static final String SAVE_PROFILE = HOME + "/save-profile-user";
    public static final String UPDATE_PASSWORD = HOME + "/change-password";

    //NUEVAS RUTAS
    public static final String HOME_PERIODS = HOME + "/home-periods";
    public static final String LEVELS = HOME + "/levels-by-student/";
    public static final String SUBJECTS = HOME + "/subjects-by-student/";
    public static final String COURSES = HOME + "/courses-by-subject/";
    public static final String MY_COURSES = HOME + "/my-courses";
    public static final String MY_LEVELS = HOME + "/my-levels";
    public static final String TASKS = HOME + "/tasks-by-course/";
    public static final String TASKS_TODAY = HOME + "/task-for-today";
    public static final String FILES = HOME + "/detail-task/";


    public static final String SPECIALTIES = HOME + "/app-specialties";
    public static final String DOCTORS = HOME + "/app-doctors";
    public static final String TIMES = HOME + "/app-times";
    public static final String REQUEST_APPOINTMENT = HOME + "/app-store-appointment";
    public static final String MY_APPOINTMENTS = HOME + "/app-get-my-appointments";
    public static final String MY_APPOINTMENTS_HISTORY = HOME + "/app-get-my-appointments-history";
    public static final String MY_TREATMENTS = HOME + "/app-get-my-treatments";
    public static final String TREATMENT_DETAIL = HOME + "/app-get-detail-treatment";
    public static final String MY_PAYMENTS = HOME + "/app-get-my-payments";
    public static final String PAYMENT_DETAIL = HOME + "/app-get-detail-payment";
    public static final String DELIVERY_TASK = HOME + "/delivery-task";
    public static final String EDUCATION = HOME + "/education";


    public static int LEVEL_ID = 0;
    public static int SUBJECT_ID = 0;
    public static int PERIOD_ID = 0;
    public static int COURSE_ID = 0;
    public static int TASK_ID = 0;
    public static int STUDENT_ID = 0;
    public static int DELIVERY_COUNT = 0;
    public static String STATUS_TASK = "Abierto";

    //TASK DETAIL
    public static String NAME_TASK = "";
    public static String DESCRIPTION_TASK = "";
    public static String START_DATE_TASK = "";
    public static String END_DATE_TASK = "";
    public static String END_TIME_TASK = "";
    public static String STATUS_TASK_DT = "";

}
