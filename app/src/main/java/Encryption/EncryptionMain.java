package Encryption;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.Algorithms.R;

import Encryption.Algorithms.AES;
import Encryption.Algorithms.DES;
import Encryption.Algorithms.Vigenere;

public class EncryptionMain extends Fragment {
    private String message;
    private String key;
    private Button Switch;
    private TextView Answer;
    private EditText Textfield_Text;
    private EditText Textfield_Key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // to load text encryption screen
        View view = inflater.inflate(R.layout.encryption_main, container, false);


        Switch = view.findViewById(R.id.Swtich);
        Answer = view.findViewById(R.id.Answer);
        Textfield_Text = view.findViewById(R.id.TextArea);
        Textfield_Key = view.findViewById(R.id.Key);

        return view;
    }

    // encrypt user msg based on selected algorithm and entered key
    public void encrypt(View view) throws Exception {

        if (Textfield_Text.length() == 0||Textfield_Key.length() == 0) {
            Toast.makeText(view.getContext(), "Enter a message/key to Encrypt", Toast.LENGTH_SHORT).show();
            return;
        }
        message = String.valueOf(Textfield_Text.getText());
        key = String.valueOf(Textfield_Key.getText());
        String Algorithm = String.valueOf(Switch.getText());
        switch (Algorithm) {
            case "Advanced Encryption Standard":
                AES aes = new AES();
                String enc = aes.AESencrypt(key.getBytes("UTF-16LE"), message.getBytes("UTF-16LE"));
                Answer.setText(enc);
                break;
            case "Triple Data Encryption Standard":
                DES des = new DES();
                String encData = des.encrypt(key.getBytes("UTF-16LE"), message.getBytes("UTF-16LE"));
                Answer.setText(encData);
                break;

            case "Vigenere Cipher":

                for (char i : message.toUpperCase().toCharArray()) {
                    if (i < 'A' || i > 'Z') {
                        Toast.makeText(view.getContext(), "Only Letters are allowed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for (char i : key.toUpperCase().toCharArray()) {
                    if (i < 'A' || i > 'Z') {
                        Toast.makeText(view.getContext(), "Only Letters are allowed as keys", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Vigenere v = new Vigenere();
                Answer.setText(v.Vigenereencrypt(message, key));

                break;
        }
    }

    // decrypt cipher text based on selected algorithm and entered key
    public void decrypt(View view) throws Exception {
        if (Textfield_Text.length() == 0||Textfield_Key.length() == 0) {
            Toast.makeText(view.getContext(), "Enter a message/key to Decrypt", Toast.LENGTH_SHORT).show();
            return;
        }
        message = String.valueOf(Textfield_Text.getText());
        key = String.valueOf(Textfield_Key.getText());

        String SwitchValue = Switch.getText().toString();
        switch (SwitchValue) {
            case "Advanced Encryption Standard":
                AES aes = new AES();
                try {
                    String decData = aes.AESdecrypt(key, Base64.decode(message.getBytes("UTF-16LE"), Base64.DEFAULT));
                    Answer.setText(decData);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Your key is wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Triple Data Encryption Standard":
                DES des = new DES();
                try {
                    String decData = des.decrypt(key, Base64.decode(message.getBytes("UTF-16LE"), Base64.DEFAULT));
                    Answer.setText(decData);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Your key is wrong", Toast.LENGTH_SHORT).show();
                }
                break;

            case "Vigenere Cipher":
                if (Textfield_Key.length() == 0) {
                    Toast.makeText(view.getContext(), "Enter a key to Decrypt", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (char i : message.toUpperCase().toCharArray()) {
                    if (i < 'A' || i > 'Z') {
                        Toast.makeText(view.getContext(), "Only Letters are allowed here", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for (char i : key.toUpperCase().toCharArray()) {
                    if (i < 'A' || i > 'Z') {
                        Toast.makeText(view.getContext(), "Only Letters are allowed as key", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Vigenere v = new Vigenere();
                Answer.setText(v.Vigeneredecrypt(message, key));
                break;


        }

    }


    // function called when user want to change algo
    public void switchAlgho(View view) {
        reset(null);
        String SwitchValue = Switch.getText().toString();
        switch (SwitchValue) {
            case "Advanced Encryption Standard":
                Switch.setText("Triple Data Encryption Standard");
                break;
            case "Triple Data Encryption Standard":
                Switch.setText("Vigenere Cipher");
                break;
            case "Vigenere Cipher":
                Switch.setText("Advanced Encryption Standard");
                break;


        }
    }


    // reset text fields and make them empty
    public void reset(View view) {
        Textfield_Text.setText("");
        Textfield_Key.setText("");
        Answer.setText("");
        if(view!=null)
        Toast.makeText(view.getContext(), "All data has been deleted", Toast.LENGTH_SHORT).show();
    }


    // copy output text to user phone Clipboard
    public void copyToClipboard(View view) {

            String copyText = String.valueOf(Answer.getText());
            if (Answer.length() == 0) {
                Toast.makeText(view.getContext(), "There is no message to copy", Toast.LENGTH_SHORT).show();
                return;
            }
            int sdk = android.os.Build.VERSION.SDK_INT;

                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                        view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText("Your message :", copyText);
                clipboard.setPrimaryClip(clip);

            Toast.makeText(view.getContext(),
                    "Your message has been copied", Toast.LENGTH_SHORT).show();

    }
}





















