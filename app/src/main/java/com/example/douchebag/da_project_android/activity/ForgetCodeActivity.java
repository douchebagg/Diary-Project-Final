package com.example.douchebag.da_project_android.activity;

import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.example.douchebag.da_project_android.R;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetCodeActivity extends AppCompatActivity{

    private SharedPreferences sharedpreferences;
    private Session session;
    private ProgressDialog pdialog;

    private final String EMAIL = "douchebag.and@gmail.com";
    private final String PASSWORD = "101505joe";

    private String code, email, to, subject, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        init();
    }

    private void init(){
        sharedpreferences = getSharedPreferences("CODE_DIRECTORY", Context.MODE_PRIVATE);
        code = sharedpreferences.getString("CODE", "");
        email = sharedpreferences.getString("EMAIL_FOR_FORGET", "");
    }

    public void sendEmail(View v) {
        to = email;
        subject = getResources().getString(R.string.mail_header);
        content = getResources().getString(R.string.mail);
        content = content.replace("THIS", code);

        Log.d("htmltest", content);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        pdialog = ProgressDialog.show(this, "", "Sending Mail...", true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] params) {
            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setContent(content, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException me) {
                me.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Intent intent = new Intent(ForgetCodeActivity.this, SendSuccessActivity.class);
            startActivity(intent);
        }
    }
}