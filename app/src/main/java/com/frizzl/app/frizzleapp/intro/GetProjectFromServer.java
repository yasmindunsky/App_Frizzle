//package com.frizzl.app.frizzleapp.intro;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.frizzl.app.frizzleapp.ConnectToServer;
//import com.frizzl.app.frizzleapp.R;
//import com.frizzl.app.frizzleapp.UserProfile;
//import com.frizzl.app.frizzleapp.appBuilder.DesignScreenFragment;
//import com.frizzl.app.frizzleapp.appBuilder.UserCreatedView;
//import com.frizzl.app.frizzleapp.appBuilder.UserCreatedViewsModel;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
///**
// * Created by yasmin.dunsky on 02-Mar-18.
// */
//
//public class GetProjectFromServer extends AsyncTask<String, Void, String> {
//    private Context mContext;
//    private String attribute;
//
//    public GetProjectFromServer(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        ConnectToServer connectToServer = new ConnectToServer();
//
//        String username = strings[0];
//        attribute = strings[1];
//
//        String body = null;
//        try {
//            body = String.format("username=%s&attr=%s",
//                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
//                    URLEncoder.encode(attribute, StandardCharsets.UTF_8.name()));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        return connectToServer.postToServer("/project/getData", body, "GET");
//    }
//
//
//    protected void onPostExecute(String result) {
//        if (!result.equals("")) {
//            try {
//                String attributeString = result;
//
//                switch (attribute) {
//                    case "views":
//                        // read viewsString from JSON
//                        JSONObject reader = new JSONObject(result);
//                        attributeString = String.valueOf(reader.get(attribute));
//
//                        // parse viewString to view element
//                        DesignScreenFragment designScreenFragment = new DesignScreenFragment();
//                        Map<Integer, UserCreatedView> views = UserCreatedViewsModel.jsonToViews(mContext, attributeString);
//
//                        // save the view to the UserProfile object
//                        UserProfile.user.setViews(views);
//                        break;
//                    case "xml":
//                        UserProfile.user.setXml(attributeString);
//                        break;
//                    case "code":
//                        // trim codeString to the user's code
//                        int start = mContext.getApplicationContext().getResources().getString(R.string.code_start).length();
//                        int end = attributeString.length() - mContext.getApplicationContext().getResources().getString(R.string.code_end).length();
//                        if (!attributeString.equals("null")) {
//                            attributeString = attributeString.substring(start, end); //TODO: Yasmin?
//                        }
//                        // save code to user profile
//                        UserProfile.user.setJava(attributeString);
//                        break;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
////    private void saveViewsToUser(String viewsString) {
////        DesignScreenFragment graphicEditFragment = new DesignScreenFragment();
////        Map<Integer, UserCreatedView> views = graphicEditFragment.jsonToViews(mContext, viewsString);
////        UserProfile.user.setViews(views);
////    }
//}