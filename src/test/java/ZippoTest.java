import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseLogSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {
        given()
                //Hazirlik ishlemleri: (token, send body, parametreler)
                .when()
                //end point (url), metodu

                .then()
        //assertion, test, data ishlemleri

        ;
    }

    @Test
    public void ststusCodeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    //body nin logonu goster donen body Json datasi (butununu istersen log.all deyede bilirsin)
                .statusCode(200)    // doniush kodu 200 mu kontrol amacli


        ;


    }

    @Test
    public void contentTypeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    //body nin logonu goster donen body Json datasi (butununu istersen log.all deyede bilirsin)
                .statusCode(200)    // doniush kodu 200 mu kontrol amacli
                .contentType(ContentType.JSON)   // donen sonuc JAson mu? assert gibi


        ;


    }


    @Test
    public void checkCountryInResponseBodyTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()    //body nin logonu goster donen body Json datasi (butununu istersen log.all deyede bilirsin)
                .statusCode(200)    // doniush kodu 200 mu kontrol amacli
                //datayi hic dishari cikartmadan kontrol etdik
                .body("country", equalTo("United States")) // bodynin country deyishkeni buna esitmi? kontrol amacli


        ;

    }

    /*
    PM                            RestAssured
body.country                  body("country")
body.'post code'              body("post code")
body.places[0].'place name'   body("places[0].'place name'")
body.places.'place name'      body("places.'place name'")
bütün place nameleri bir arraylist olarak verir

{
    "post code": "90210",
    "country": "United States",
    "country abbreviation": "US",
    "places": [
        {
            "place name": "Beverly Hills",
            "longitude": "-118.4065",
            "state": "California",
            "state abbreviation": "CA",
            "latitude": "34.0901"
        }
    ]
}

     */
    @Test
    public void checkCountryStateInResponseBodyTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                // .log().body()    //body nin logonu goster donen body Json datasi (butununu istersen log.all deyede bilirsin)
                .statusCode(200)    // doniush kodu 200 mu kontrol amacli
                //datayi hic dishari cikartmadan kontrol etdik
                .body("places[0].state", equalTo("California")) // bodynin country deyishkeni buna esitmi? kontrol amacli


        ;

    }

    @Test
    public void checkHasItemTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")


                .then()
                //.log().body()
                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü")) //bwtwn palce namelerin her hangi birinde Dörtağaç Köyü varmi?


        ;

    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1))    // size si 1 mi?


        ;

    }

    @Test
    public void combiningTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                //.log().body()
                .statusCode(200)
                .body("places", hasSize(1))    // size si 1 mi?
                .body("places[0].'place name'", equalTo("Beverly Hills")) //veri patindaki deyer buna esitmi?
                .body("places.state", hasItem("California"))  //veri patindaki list bu iteme sahibmi?


        ;

    }


    @Test
    public void pathParamTest() {
        /*
        http://api.zippopotam.us/us/90210    path PARAM

        https://sonuc.osym.gov.tr/Sorgu.aspx?SonucID=9617  Query PARAM
         */

        given()
                .pathParam("ulke", "us")
                .pathParam("postaKod", 90210)
                .log().uri()   //reguest urli linki alabiliyorum

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")


                .then()
                .statusCode(200)
        // .log().body()


        ;


    }

    @Test
    public void queryParamTest() {


        given()
                .param("page", 1)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users") //conuc page=1


                .then()
                .statusCode(200)
        //.log().body()


        ;


    }

    @Test
    public void queryParamTest2() {
/*
https://gorest.co.in/public/v1/users?page=2
@Test
    public void queryParamTest2()
    {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.


    }
 */

        for (int i = 1; i < 11; i++) {


            given()

                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users") //conuc page=1

                    .then()
                    .statusCode(200)
                    // .log().body()
                    .body("meta.pagination.page", equalTo(i))


            ;


        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void Setup() {

        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void requestResponseSpecification() {

        given()
                .param("page", 1)
                .spec(requestSpec)

                .when()
                .get("/users")


                .then()
                .spec(responseSpec)


        ;


    }

    @Test
    public void exactingJsonpath() {
        String countryName =
                given()

                        .when()

                        .get("http://api.zippopotam.us/us/90210")
                        .then()

                        .log().body()
                        .extract().path("country");
        System.out.println("countryName = " + countryName);

    }

    @Test
    public void exactingJsonpath2() {
        String placeName =
                given()

                        .when()

                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        //.log().body()
                        .extract().path("places[0].'place name'");
        System.out.println("Place Name = " + placeName);

    }

    @Test
    public void exactingJsonpath3() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki limit bilgisini yazdırınız.
        int limit =
                given()

                        .when()

                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit");
        System.out.println("Place Name = " + limit);

    }

    @Test
    public void exactingJsonpath4() {
        List<Integer> id =
                given()

                        .when()

                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id");  // butun id leri aldik

        System.out.println("id = " + id);

    }

    @Test
    public void exactingJsonpath5() {
        List<String> name =
                given()

                        .when()

                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name");  // butun id leri aldik

        System.out.println("Name = " + name);

    }

    @Test
    public void exactingJsonpathAll() {
        Response data =
                given()

                        .when()

                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response() // donen tum datayi verir
                ;

        List<Integer> ids = data.path("data.id");
        List<String> names = data.path("data.name");
        int limit = data.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("names = " + names);
        System.out.println("ids = " + ids);

        Assert.assertTrue(names.contains("Dakshayani Pandey"));
        Assert.assertTrue(ids.contains(1203767));
        Assert.assertEquals(limit, 10, "test sonucu hatali");

    }

    @Test
    public void extractJsonAll() {
        Location locationnesnesi =
                given()

                        .when()

                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        // .log().body()
                        .extract().body().as(Location.class)//location sablonuna gore bunu donushtur
                ;

        System.out.println("Location Nesnesi = " + locationnesnesi.getCountry());

        for (Place p : locationnesnesi.getPlaces()) {

            System.out.println("p = " + p);
        }

        System.out.println("locationnesnesi.getPlaces().get(0).getPlacename() = " + locationnesnesi.getPlaces().get(0).getPlacename());

    }
    @Test
    public void extractJsonAll_POJO() {
        Location adana=
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")


                .then()
               // .log().body()
                .statusCode(200)
                .extract().body().as(Location.class)



        ;
        for (Place p: adana.getPlaces()){
            if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü"));
            System.out.println("p = " + p);
        }

    }


}
