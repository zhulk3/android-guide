package com.longkai.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public class FlickrFetcher {
  private static final String API_KEY = "e5c12016f1cfadecdd5fe45e75d964de";
  private static final String FETCH_RECENT_METHOD = "flickr.photos.getRecent";
  private static final String SEARCH_METHOD = "flickr.photos.search";
  public static final Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/").buildUpon()
      .appendQueryParameter("api_key", API_KEY).appendQueryParameter("format", "json")
      .appendQueryParameter("nojsoncallback", "1").appendQueryParameter("extras", "url_s")
      .build();

  public byte[] getUrlBytes(String urlSpec) throws IOException {
    URL url = new URL(urlSpec);
    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      InputStream inputStream = httpURLConnection.getInputStream();
      if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new IOException(httpURLConnection.getResponseMessage() + ":with " + urlSpec);
      }
      int byteRead = 0;
      byte[] buffer = new byte[1024];
      while ((byteRead = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, byteRead);
      }
      outputStream.close();
      return outputStream.toByteArray();
    } finally {
      httpURLConnection.disconnect();
    }
  }

  public String getUrlString(String urlSpec) throws IOException {
    return new String(getUrlBytes(urlSpec));
  }

  private List<GalleryItem> downloadGalleryItems(String url) {
    List<GalleryItem> items = new ArrayList<>();
    try {
      String jsonString = getUrlString(url);
      JSONObject jsonObject = new JSONObject(jsonString);
      parseItems(items, jsonObject);
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }
    return items;
  }

  private String buildUrl(String method, String text) {
    Uri.Builder builder = ENDPOINT.buildUpon().appendQueryParameter("method", method);
    if (SEARCH_METHOD.equals(method)) {
      builder.appendQueryParameter("text", text);
    }
    return builder.toString();
  }

  public List<GalleryItem> fetchRecentPhotos() {
    String url = buildUrl(FETCH_RECENT_METHOD, null);
    return downloadGalleryItems(url);
  }


  public List<GalleryItem> searchPhotos(String text) {
    String url = buildUrl(SEARCH_METHOD, text);
    return downloadGalleryItems(url);
  }

  public void parseItems(List<GalleryItem> items, JSONObject jsonObject) throws JSONException {
    JSONObject photosJsonObject = jsonObject.getJSONObject("photos");
    JSONArray photosJsonArray = photosJsonObject.getJSONArray("photo");
    for (int i = 0; i < photosJsonArray.length(); i++) {
      JSONObject photoJsonObject = (JSONObject) photosJsonArray.get(i);
      GalleryItem item = new GalleryItem();
      item.setId(photoJsonObject.getString("id"));
      item.setCaption(photoJsonObject.getString("title"));
      if (!photoJsonObject.has("url_s")) {
        continue;
      }
      item.setUrl(photoJsonObject.getString("url_s"));
      item.setOwner(photoJsonObject.getString("owner"));
      items.add(item);
    }
  }
}
