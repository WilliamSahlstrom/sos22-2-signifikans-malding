package fi.arcada.projekt_chi2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Deklarera 4 Button-objekt
    Button btn1, btn2, btn3, btn4, btn5;
    // Deklarera 4 heltalsvariabler för knapparnas värden
    int val1, val2, val3, val4;
    TextView textOut, textView2, textView8, textView9, textViewCol1, textViewCol2, textViewCol3, textViewCol4, textViewRow1, textViewRow2, textViewRow3;
    int button1, button2, button3, button4;



    //Objekt för preferences
    SharedPreferences sharedPref;
    SharedPreferences.Editor prefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCol1 = findViewById(R.id.textViewCol1);
        textViewCol2 = findViewById(R.id.textViewCol2);
        textViewCol3 = findViewById(R.id.textViewCol3);
        textViewCol4 = findViewById(R.id.textViewCol4);
        textViewRow1 = findViewById(R.id.textViewRow1);
        textViewRow2 = findViewById(R.id.textViewRow2);
        textViewRow3 = findViewById(R.id.textViewRow3);
        textOut = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);



        //Username har vi skrivit in i Settings activity
        textViewCol1.setText(String.format("%s", sharedPref.getString("Kolumn1", null)));
        textViewCol2.setText(String.format("%s", sharedPref.getString("Kolumn2", null)));
        textViewRow1.setText(String.format("%s", sharedPref.getString("Rad1", null)));
        textViewRow2.setText(String.format("%s", sharedPref.getString("Rad2", null)));
        textViewRow3.setText(String.format("%s", sharedPref.getString("Rad1", null)));
        textView2.setText(String.format("%s", sharedPref.getString("Signifikansnivå", null)));


        // Koppla samman Button-objekten med knapparna i layouten
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);

        //SharedPreferences.Editor prefEditor = sharedPref.edit();
        /*prefEditor.putInt("Signifikansnivå",5);
        prefEditor = sharedPref.edit();
        prefEditor.apply();*/

        //btn1.setText((String.format("%d", sharedPref.getString("button1",null))));

        //Username har vi skrivit in i Settings activity
        /*textView2.setText(String.format("Välkommen tillbaka %s", sharedPref.getString("userName", null)));
        //Hämta ut det gamla värdet, addera 1, och spara igen
        prefEditor = sharedPref.edit();
        prefEditor.putInt("btn1", sharedPref.getInt("button1", 0));*/
        /*prefEditor.putInt("button2", sharedPref.getInt("button2", 0));
        prefEditor.putInt("button3", sharedPref.getInt("button3", 0));
        prefEditor.putInt("button4", sharedPref.getInt("button4", 0));*/
        //prefEditor.apply();
        //Hämta ut värdet
        //button1= sharedPref.getInt("button1", 0);


        //textOut.setText(String.format("Appen har startats %d ", launchCount));

    }
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     *  Klickhanterare för knapparna
     */
    public void buttonClick(View view) {

        // Skapa ett Button-objekt genom att type-casta (byta datatyp)
        // på det View-objekt som kommer med knapptrycket
        Button btn = (Button) view;

        // Kontrollera vilken knapp som klickats, öka värde på rätt vaiabel
        if (view.getId() == R.id.button1) val1++;
        if (view.getId() == R.id.button2) val2++;
        if (view.getId() == R.id.button3) val3++;
        if (view.getId() == R.id.button4) val4++;
        if (view.getId() == R.id.button5) {
            val1 = 0;
            val2 = 0;
            val3 = 0;
            val4 = 0;
        }

        // Slutligen, kör metoden som ska räkna ut allt!
        calculate();
    }

    /**
     * Metod som uppdaterar layouten och räknar ut själva analysen.
     */
    public void calculate() {

        // Uppdatera knapparna med de nuvarande värdena
        btn1.setText(String.valueOf(val1));
        btn2.setText(String.valueOf(val2));
        btn3.setText(String.valueOf(val3));
        btn4.setText(String.valueOf(val4));


        //Procenterna
        double percent = Significance.calcPercent(val1, val3);
        double percent2 = Significance.calcPercent(val2, val4);
        textViewCol3.setText(String.format("%s", sharedPref.getString("Kolumn1", null) + ": " + percent + "%"));
        textViewCol4.setText(String.format("%s", sharedPref.getString("Kolumn2", null) + ": " + percent2 + "%"));


        // Mata in värdena i Chi-2-uträkningen och ta emot resultatet
        // i en Double-variabel
        double chi2 = Significance.chiSquared(val1, val2, val3, val4);


        // Mata in chi2-resultatet i getP() och ta emot p-värdet
        double pValue = Significance.getP(chi2);



        /**
         *  - Visa chi2 och pValue åt användaren på ett bra och tydligt sätt!
         *
         *  - Visa procentuella andelen jakande svar inom de olika grupperna.
         *    T.ex. (val1 / (val1+val3) * 100) och (val2 / (val2+val4) * 100
         *
         *  - Analysera signifikansen genom att jämföra p-värdet
         *    med signifikansnivån, visa reultatet åt användaren
         *
         */

        // %.2f i String.format() avrundar till två decimaler
        String uträkningStr = String.format("%s: %.2f",
                "Chi resultatet är", chi2
        );
        String uträkningPStr = String.format("%s: %.2f",
                "P-värdet är", pValue);

        String uträkningRes = String.format("%s: %.2f %s",
                "Resultatet är med", (1-pValue)*100, "% sannolikhet inte oberoende och kan betraktas som signifikant");

        String uträkningRes2 = String.format("%s: %.2f %s",
                "Resultatet är med", (1-pValue)*100, "% sannolikhet inte oberoende och kan inte betraktas som signifikant");

        textOut.setText(uträkningStr);

        textView8.setText(uträkningPStr);

        textView9.setText(uträkningRes);

        String test = textView2.getText().toString();
        System.out.println(test);

        double signifikans1 =Double.parseDouble(test);
        if (pValue <= signifikans1) {
            textView9.setText(uträkningRes);
        }else {
            textView9.setText(uträkningRes2);
        }
    }

}