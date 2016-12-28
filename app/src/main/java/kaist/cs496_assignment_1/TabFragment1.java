package kaist.cs496_assignment_1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class TabFragment1 extends Fragment {
    private static final int CODE_READ = 42;
    public ArrayList<HashMap<String, String>> contactList;

    public Button btnLoadContacts;
    public Button btnLoadJSON;
    public ListView contactListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        btnLoadContacts = (Button) view.findViewById(R.id.loadContact);
        btnLoadJSON = (Button) view.findViewById(R.id.loadJSON);
        contactListView = (ListView) view.findViewById(R.id.contactList);

        btnLoadContacts.setOnClickListener(loadContactOnClicked);
        btnLoadJSON.setOnClickListener(loadJSONOnClicked);
        contactList = new ArrayList<>();

        return view;
    }

    View.OnClickListener loadContactOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Load Contact!", Toast.LENGTH_SHORT).show();
            clearAll();
            readContact(getContext());
        }
    };

    View.OnClickListener loadJSONOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Load JSON!", Toast.LENGTH_SHORT).show();
            clearAll();
            readJSONFile();
        }
    };

    public void displayList() {
        ListAdapter adapter = new SimpleAdapter(
                getActivity(),
                contactList,
                R.layout.list_item,
                new String[]{"name", "mobile"},
                new int[]{R.id.name, R.id.mobile});
        contactListView.setAdapter(adapter);
    }

    public void clearAll() {
        contactList.clear();
        displayList();
    }

    public void readContact(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final MainActivity activity = (MainActivity) getActivity();
            int permissionResult = ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_CONTACTS);

            if (permissionResult == PackageManager.PERMISSION_DENIED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("Need permission").setMessage("To use this function, We need permission for \"READ_CONTACTS\". Continue?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity, "Cancelled the function", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1000);
                }
            } else {
                Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            int id_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                            int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            int mobile_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                            String id = cursor.getString(id_idx);
                            String name = cursor.getString(name_idx);
                            String mobile = cursor.getString(mobile_idx);

                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("id", id);
                            contact.put("name", name);
                            contact.put("mobile", mobile);

                            System.out.println("id: " + id + " / name : " + name + " / mobile : " + mobile);
                            contactList.add(contact);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                displayList();
            }
        }
        else {
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int id_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                        int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        int mobile_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                        String id = cursor.getString(id_idx);
                        String name = cursor.getString(name_idx);
                        String mobile = cursor.getString(mobile_idx);

                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("mobile", mobile);

                        System.out.println("id: " + id + " / name : " + name + " / mobile : " + mobile);
                        contactList.add(contact);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            displayList();
        }
    }



    public void readJSONFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        startActivityForResult(intent, CODE_READ);
    }

    public void JSONProcessing(String json) {
        if (json != null) {
            GetContacts getContacts = new GetContacts(json);
            getContacts.execute();
        } else {
            System.out.println("JSON Processing: json string is null");
        }
    }

    public void JSONContactsParser(String json) {
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray contacts = jsonObject.getJSONArray("contacts");

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String id = c.getString("id");
                    String name = c.getString("name");
                    String email = c.getString("email");
                    // String address = c.getString("address");
                    // String gender = c.getString("gender");

                    JSONObject phone = c.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    // String home = phone.getString("home");
                    // String office = phone.getString("office");

                    HashMap<String, String> contact = new HashMap<>();

                    contact.put("id", id);
                    contact.put("name", name);
                    contact.put("email", email);
                    contact.put("mobile", mobile);

                    contactList.add(contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("json string is null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        final ContentResolver cr = getActivity().getContentResolver();
        final Uri uri = data != null ? data.getData() : null;

        if (uri != null) {
            System.out.println("isDocumentUri = " + DocumentsContract.isDocumentUri(getActivity(), uri));
        } else {
            return;
        }

        if (requestCode == CODE_READ) {
            try {
                cr.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (SecurityException e) {
                System.out.println("FAILED TO TAKE PERMISSION: " + e);
            }

            InputStream is = null;
            try {
                is = cr.openInputStream(uri);
                JSONProcessing(getJSONStringFromInputStream(is));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeQuietly(is);
            }
        }
        else {
            System.out.println("Invalid request");
        }
    }

    public String getJSONStringFromInputStream(InputStream is) {
        String json = null;
        ByteArrayOutputStream readStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        try {
            if (is != null) {
                while ((count = is.read(buffer)) != -1) {
                    readStream.write(buffer, 0, count);
                }
                json = new String(readStream.toByteArray());
            } else {
                System.out.println("INPUT STREAM IS NULL");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        String json;

        GetContacts(String json) {
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONContactsParser(json);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    contactList,
                    R.layout.list_item,
                    new String[]{"name", "mobile"},
                    new int[]{R.id.name, R.id.mobile});
            contactListView.setAdapter(adapter);
        }
    }
}
