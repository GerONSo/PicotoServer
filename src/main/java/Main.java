import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://194.87.103.212:4567/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerService api = retrofit.create(ServerService.class);
        File file = new File("/home/maxim/IdeaProjects/content.jpg");
        MultipartBody.Part filePart = MultipartBody.Part.
                createFormData("file", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file));
        Call<String> call = api.upload(filePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        File file = new File("/home/maxim/PicotoServer/result.jpg");
                        FileOutputStream fos = new FileOutputStream(file);
                        String s = response.body();
                        byte[] bytes = Base64.getDecoder().decode(s);
                        fos.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println("Failed");
            }
        });
    }
}
