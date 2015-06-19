package azaza.myapplication.Libs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.MainActivity;

/**
 * Класс-помошник для распознавания речи
 */
public class SpeechRecognitionHelper extends MainActivity {

    public static int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static Intent value;

    /**
     * Запускает процесс распознавания. Проверяет наличие активити для распознования речи.
     * Если активити нет, отправляет пользователя в маркет установить Голосовой Поиск
     * Google. Если активи для распознования есть, то отправляет Intent для ее запуска.
     *
     * @param ownerActivity активити, которая инициировала процесс распозвания
     */
    public static void run(Activity ownerActivity) {
        // проверяем есть ли активити для распознавания
        if (isSpeechRecognitionActivityPresented(ownerActivity) == true) {
            // если есть - запускаем распознавание
            startRecognitionActivity(ownerActivity);
        } else {
            // начинаем процесс установки
            installGoogleVoiceSearch(ownerActivity);
        }
    }

    /**
     * Проверяет наличие активити способной выполнить распознавание речи
     *
     * @param ownerActivity активити, которая запросила проверку
     * @return true - если есть, false - если такой активити нет
     */
    private static boolean isSpeechRecognitionActivityPresented(Activity ownerActivity) {
        try {
            // получаем экземпляр менеджера пакетов
            PackageManager pm = ownerActivity.getPackageManager();
            // получаем список активити способных обработать запрос на распознавание
            List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

            if (activities.size() != 0) {	// если список не пустой
                return true;				// то умеем распознавать речь
            }
        } catch (Exception e) {

        }

        return false; // не умеем распозновать речь
    }

    /**
     * Отпавляет Intent с запросом на распознавание речи
     * @param ownerActivity иниировавшая запрос активити
     */
    private static void startRecognitionActivity(Activity ownerActivity) {

        // создаем Intent с действием RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // добаляем дополнительные параметры:
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Голосовой поиск Inforino");	// текстовая подсказка пользователю
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);	// модель распознавания
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);	// количество резальтатов, которое мы хотим получить
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ua-UA");

        // стартуем активити и ждем от нее результата
        ownerActivity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * Запрашивает разрешения на становку Голосового Поиска Google, отображая диалог. Если разрешение
     * получино - направляет пользователя в маркет.
     * @param ownerActivity активити иниировавшая установку
     */
    private static void installGoogleVoiceSearch(final Activity ownerActivity) {

        // создаем диалог, который спросит у пользователя хочет ли он
        // установить Голосовой Поиск
        Dialog dialog = new AlertDialog.Builder(ownerActivity)
                .setMessage("Для распознавания речи необходимо установить \"Голосовой поиск Google\"")	// сообщение
                .setTitle("Внимание")	// заголовок диалога
                .setPositiveButton("Установить", new DialogInterface.OnClickListener() {	// положительная кнопка

                    // обработчик нажатия на кнопку Установить
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // создаем Intent для открытия в маркете странички с приложением
                            // Голосовой Поиск имя пакета: com.google.android.voicesearch
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.voicesearch"));
                            // настраиваем флаги, чтобы маркет не папал к в историю нашего приложения (стек активити)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            // отправляем Intent
                            ownerActivity.startActivity(intent);
                        } catch (Exception ex) {
                            // неудалось открыть маркет
                            // например из-за того что он не установлен
                            // ничего не подалаешь
                        }
                    }})

                .setNegativeButton("Отмена", null)	// негативная кнопка
                .create();

        dialog.show();	// показываем диалог
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if it’s speech recognition results
        // and process finished ok
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {

            // receiving a result in string array
            // there can be some strings because sometimes speech recognizing inaccurate
            // more relevant results in the beginning of the list
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            // in “matches” array we holding a results... let’s show the most relevant
            if (matches.size() > 0) Toast.makeText(this, (Integer) matches.get(0), Toast.LENGTH_LONG).show();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
