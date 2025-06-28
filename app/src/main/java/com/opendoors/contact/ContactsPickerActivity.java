package com.opendoors.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.compose.setContent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.lazy.LazyColumn;
import androidx.compose.foundation.lazy.items;
import androidx.compose.material.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;

public class ContactsPickerActivity extends Activity {

    ListView contactsChooser;
    Button btnDone;
    EditText txtFilter;
    TextView txtLoadInfo;
    ContactsListAdapter contactsListAdapter;
    ContactsLoader contactsLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent {
            ContactsPickerScreen(
                    onDone = { /* TODO: Return selected contacts */ finish() }
            )
        };
    }

    @Composable
    fun ContactsPickerScreen(onDone: () -> Unit) {
        var filter by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var contacts by remember { mutableStateOf(listOf("Contact 1", "Contact 2", "Contact 3")) }
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(
                    value = filter,
                    onValueChange = { filter = it },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
            )
            if (isLoading) {
                Text("Loading...", modifier = Modifier.padding(vertical = 8.dp))
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(contacts.filter { it.contains(filter, ignoreCase = true) }) { contact ->
                    Text(contact, modifier = Modifier.padding(8.dp))
                    Divider()
                }
            }
            Button(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Done")
            }
        }
    }

    private void loadContacts(String filter){

        if(contactsLoader!=null && contactsLoader.getStatus()!= AsyncTask.Status.FINISHED){
            try{
                contactsLoader.cancel(true);
            }catch (Exception e){

            }
        }
        if(filter==null) filter="";

        try{
            //Running AsyncLoader with adapter and  filter
            contactsLoader = new ContactsLoader(this,contactsListAdapter);
            contactsLoader.txtProgress = txtLoadInfo;
            contactsLoader.execute(filter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }




}
