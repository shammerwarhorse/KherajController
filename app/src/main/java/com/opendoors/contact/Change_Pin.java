package com.opendoors.contact;

import androidx.activity.compose.setContent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.Alignment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Change_Pin extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        setContent {
            ChangePinScreen(
                onBack = { finish() },
                onSave = { newPin, email ->
                    editor.putString("pinchanged",newPin);
                    editor.commit();
                    Toast.makeText(this, "PIN changed!", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onCancel = { finish() }
            )
        };
    }

    @Composable
    fun ChangePinScreen(
        onBack: () -> Unit,
        onSave: (String, String) -> Unit,
        onCancel: () -> Unit
    ) {
        var newPin by remember { mutableStateOf("") }
        var confirmPin by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        val isSaveEnabled = newPin.length == 4 && newPin == confirmPin && email.isNotBlank()
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                elevation = 10.dp,
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Change PIN",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = newPin,
                onValueChange = { if (it.length <= 4) newPin = it },
                label = { Text("New PIN") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                value = confirmPin,
                onValueChange = { if (it.length <= 4) confirmPin = it },
                label = { Text("Retype PIN") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onCancel) { Text("Cancel") }
                Button(onClick = { onSave(newPin, email) }, enabled = isSaveEnabled) { Text("Save") }
            }
        }
    }
}