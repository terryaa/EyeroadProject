package com.example.hoyoung.eyeload;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Jin on 2016-11-29.
 */

public class BuildingDAO extends DAO {

    private ArrayList<BuildingDTO> arrayListBuildingDTO;
    private BuildingDTO buildingDTOSelected;

    public ArrayList<BuildingDTO> getArrayListBuildingDTO() {
        return arrayListBuildingDTO;
    }

    public BuildingDTO getBuildingDTOSelected() {
        return buildingDTOSelected;
    }

    public BuildingDAO()

    {
        arrayListBuildingDTO = new ArrayList<BuildingDTO>();
        buildingDTOSelected = new BuildingDTO();
    }

    //안드로이드->DB로 값을 삽입하기 위한 함수
    public boolean insert(BuildingDTO dto)
    {

        try{
            //매개변수로 받은 객체의 정보를 빼오는 부분
            String name = dto.getName();
            String x = String.valueOf(dto.getX());
            String y = String.valueOf(dto.getY());
            String z = String.valueOf(dto.getZ());
            String information = dto.getInformation();
            String image = dto.getImage();

            String link="http://210.94.194.201/insertBuilding.php";//변수들을 보낼 link
            //String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            //id값은 자동으로 설정됨
            String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("x", "UTF-8") + "=" + URLEncoder.encode(x, "UTF-8");
            data += "&" + URLEncoder.encode("y", "UTF-8") + "=" + URLEncoder.encode(y, "UTF-8");
            data += "&" + URLEncoder.encode("z", "UTF-8") + "=" + URLEncoder.encode(z, "UTF-8");
            data += "&" + URLEncoder.encode("information", "UTF-8") + "=" + URLEncoder.encode(information, "UTF-8");
            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );//Mapping된 데이터를 PHP를 통해 처리하는 부분
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            //서버로부터 반환된 값 읽어옴
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }

            if(sb.toString().equals("success"))//성공적으로 불러왔을 시
                return true;
            else
                return false;
        }
        catch(Exception e){
            return false;
        }

    }

    //key값을 이용하여 DB의 튜플을 삭제
    public boolean delete(String key){

        try{

            String link="http://210.94.194.201/deleteBuilding.php";//key값을 보낼 link

            String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");//PHP를 통해 변수들을 Mapping하는 부분


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );//Mapping된 데이터를 PHP를 통해 처리하는 부분
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            //서버로부터 반환된 값 읽어옴
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }

            if(sb.toString().equals("success"))//성공적으로 삭제했을 시
                return true;
            else
                return false;
        }
        catch(Exception e){
            return false;
        }
    }

    //key값을 이용하여 선택된 튜플을 DB로부터 받아오는 함수
    public boolean select(int key)
    {
        try {

            String link = "http://210.94.194.201/selectBuilding.php";//튜플에 대한 데이터를 받아올 link

            //PHP를 통해 변수들을 Mapping하는 부분
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(key), "UTF-8");

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            wr.write(data);//Mapping된 데이터를 PHP를 통해 처리하는 부분
            wr.flush();

            StringBuilder sb = new StringBuilder();

            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json = null;
            while((json = bufferedReader.readLine())!= null){//서버로부터 반환된 값 읽어옴
                sb.append(json+"\n");
            }

            //PHP를 통해 받아온 변수들을 JSON을 이용하여 처리하는 부분
            String queryJson = sb.toString().trim();
            JSONObject jsonObj = new JSONObject(queryJson);
            JSONArray buildingInfo = jsonObj.getJSONArray("result");

            JSONObject c = buildingInfo.getJSONObject(0);

            //JSON을 이용하여 DTO에 데이터 삽입하는 부분
            BuildingDTO buildingDTO = new BuildingDTO();

            buildingDTO.setName(c.getString("name"));
            buildingDTO.setX(Double.valueOf(c.getString("x")));
            buildingDTO.setY(Double.valueOf(c.getString("y")));
            buildingDTO.setZ(Double.valueOf(c.getString("z")));
            buildingDTO.setInformation(c.getString("information"));
            buildingDTO.setImage(c.getString("image"));

            buildingDTOSelected = buildingDTO;

            return true;
        }catch (Exception e) {
            return false;
        }

    }

    //모든 Building에 대한 정보를 DB로부터 받아오는 함수
    public boolean selectAll()
    {

        try {

            BufferedReader bufferedReader = null;
            String link = "http://210.94.194.201/selectAllBuilding.php";//모든 빌딩에 대한 데이터를 받아올 link

            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while ((json = bufferedReader.readLine()) != null) {//서버로부터 반환된 값 읽어옴
                sb.append(json + "\n");
            }

            String result = sb.toString().trim();

            arrayListBuildingDTO.clear();//업데이트를 위한 초기화부분

            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonArrayBuildingDTO = null;
            jsonArrayBuildingDTO = jsonObj.getJSONArray("result");

            for (int i = 0; i < jsonArrayBuildingDTO.length(); i++) {

                //BuildingDTO 객체를 생성
                BuildingDTO buildingDTO = new BuildingDTO();

                JSONObject c = jsonArrayBuildingDTO.getJSONObject(i);
                //BuildingDTO 객체에 정보 삽입
                buildingDTO.setKey(Integer.valueOf(c.getString("id")));
                buildingDTO.setName(c.getString("name"));
                buildingDTO.setX(Double.valueOf(c.getString("x")));
                buildingDTO.setY(Double.valueOf(c.getString("y")));
                buildingDTO.setZ(Double.valueOf(c.getString("z")));
                //buildingDTO.setInformation(c.getString("information"));
                //buildingDTO.setImage(c.getString("image"));

                arrayListBuildingDTO.add(buildingDTO);//BuildingDTO 객체를 ArrayList에 삽입

            }
            return true;
        }catch(Exception e){
            return false;
        }

    }
}
