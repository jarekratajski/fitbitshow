package ch.css.ebusiness.fitbit.impl.tocken;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.services.Base64Encoder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javaslang.collection.List;
import javaslang.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;


public class FitbitConnector {

    private static final Logger logger = LoggerFactory.getLogger(FitbitConnector.class);

    public static final String API_KEY = "227S5Y";

    private String apiSecret;

    OAuth20Service service;
    private final Fitbit20 api = new Fitbit20();

    public FitbitConnector() {
        final InputStream fitbitConf = getClass().getResourceAsStream("/fitbit.properties");

        if (fitbitConf != null) {
            final Properties props = new Properties();
            try {
                props.load(fitbitConf);
                apiSecret = props.getProperty("secret");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(apiSecret)
                .debug()
                .build(api);
    }


    public Either<String, OAuth2AccessToken> getToken(String code) {
        try {

            OAuthRequest request = new OAuthRequest(
                    Verb.POST, this.service.getApi().getAccessTokenEndpoint(), service);
            request.addParameter("grant_type", "authorization_code");
            request.addParameter("client_id", API_KEY);
            request.addParameter("code", code);
            request.addParameter("redirect_uri", "http://localhost:8088/services/after");

            this.signAppRequest(request);
            final Response response = request.send();
            OAuth2AccessToken extracted = api.getAccessTokenExtractor().extract(response);

            return Either.right(extracted);
        } catch (IOException e) {
            return Either.left(e.getMessage());
        }
    }

    public Either<String, List<Integer>> getHr(String token) {
        return getActivities(token, "/1/user/-/activities/heart/date/today/1d/1min.json", "activities-heart-intraday");
    }

    public Either<String, List<Integer>> getSteps(String token) {
        return getActivities(token, "/1/user/-/activities/steps/date/today/1d/1min.json", "activities-steps-intraday");
    }

    private Either<String, List<Integer>> getActivities(String token, String url, String extractValue) {
        final OAuthRequest req = new OAuthRequest(Verb.GET, api.HOST +
                url,
                service);
        final OAuth2AccessToken aToken = new OAuth2AccessToken(token);
        signUserRequest(req, aToken.getAccessToken());
        final Response response = req.send();

        logger.debug(String.valueOf(response.getCode()));
        try {
            final String result = response.getBody();
            logger.debug(String.valueOf(result));

            JsonParser parser = new JsonParser();
            JsonElement json = parser.parse(result);
            JsonObject activities = json.getAsJsonObject().getAsJsonObject(extractValue);
            if (activities != null) {
                JsonArray array = activities.getAsJsonArray("dataset");
                List<JsonElement> allElems = List.ofAll(array);
                return Either.right(allElems
                        .map(JsonElement::getAsJsonObject)
                        .map(elem -> elem.getAsJsonPrimitive("value").getAsInt())
                        .takeRight(50));
            } else {
                return Either.left("no data from Server - possibly invalid token");
            }


        } catch (final IOException e) {
            logger.warn(e.getMessage(), e);
            return Either.left(e.getMessage());
        }
    }


    private void signAppRequest(OAuthRequest request) {
        if (API_KEY != null && apiSecret != null) {
            request.addHeader("Authorization", "Basic " + Base64Encoder.getInstance().encode(String.format("%s:%s", new
                    Object[]{API_KEY, apiSecret}).getBytes(Charset.forName("UTF-8"))));
        }
    }

    private void signUserRequest(OAuthRequest request, String token) {

        request.addHeader("Authorization", "Bearer " + token);

    }

    private static final class Fitbit20 extends DefaultApi20 {

        public static final String HOST = "https://api.fitbit.com";

        @Override
        public String getAccessTokenEndpoint() {
            return HOST + "/oauth2/token";
        }

        @Override
        protected String getAuthorizationBaseUrl() {
            return null;
        }
    }
}
