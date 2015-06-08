package azaza.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import azaza.myapplication.DataBase.DB;
import azaza.myapplication.GlobalData.PhoneData;

/**
 * Created by Alex on 05.06.2015.
 */
public class CallActivity extends Activity{

    TextView phone, date;
    Button speak;
    EditText comment;
    String text;
    DB db = new DB(this);
    ArrayList<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        speak = (Button) findViewById(R.id.button);
        phone = (TextView) findViewById(R.id.Phone);
        date = (TextView) findViewById(R.id.Date);
        comment = (EditText) findViewById(R.id.comment);

        phone.setText(PhoneData.PHONE);
        date.setText(PhoneData.DATE);

        speak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // This are the intents needed to start the Voice recognizer
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU");
                i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

                startActivityForResult(i, 1010);
            }
        });
        if (savedInstanceState != null) {
            results = savedInstanceState.getStringArrayList("results");

            if (results != null)
               comment.setText(results.toString());

        }

    }

    public void onSave(View v){
        db.open();
        text = (comment.getText().toString());
        db.addRec(PhoneData.myTYPE,  PhoneData.PHONE, PhoneData.DATE, text);
        this.finish();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onCLose(View v){
        this.finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // retrieves data from the VoiceRecognizer
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            comment.setText(results.toString());
//            options.setAdapter(new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_1, results));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // This should save all the data so that when the phone changes
        // orientation the data is saved
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("results", results);
    }

}
