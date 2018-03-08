package spark;


import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.HashtagEntity;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterBeanTest {

    public static String TEST_TWEET = "This is a test tweet";
    public static String TEST_USER = "testuser";
    public static Date CREATED_DATE = new Date();

    @Mock
    Status mockStatus;

    @Mock
    User mockUser;

    @Mock
    Place mockPlace;


    private HashtagEntity[] createMockHashtags(){

        HashtagEntity h1 = mock(HashtagEntity.class);
        HashtagEntity h2 = mock(HashtagEntity.class);
        when(h1.getText()).thenReturn("#hashtag1");
        when(h2.getText()).thenReturn("#hashtag2");
        return new HashtagEntity[]{ h1, h2};
    }

    @Before
    public void initMocks(){

        MockitoAnnotations.initMocks(this);

        when(mockStatus.getText()).thenReturn(TEST_TWEET);
        when(mockStatus.getUser()).thenReturn(mockUser);
        when(mockStatus.getCreatedAt()).thenReturn(CREATED_DATE);

        when(mockUser.getName()).thenReturn(TEST_USER);
        when(mockUser.getFollowersCount()).thenReturn(456);

        when(mockStatus.getFavoriteCount()).thenReturn(13);
        when(mockStatus.getRetweetCount()).thenReturn(123);

        when(mockStatus.getLang()).thenReturn("en");
        HashtagEntity[] entities = createMockHashtags();
        when(mockStatus.getHashtagEntities()).thenReturn(entities);
        when(mockStatus.isPossiblySensitive()).thenReturn(false);
        when(mockStatus.isRetweet()).thenReturn(false);
        when(mockStatus.isFavorited()).thenReturn(true);

        when(mockStatus.getPlace()).thenReturn(mockPlace);

        when(mockPlace.getCountry()).thenReturn("United Kingdom");
    }

    @Test
    public void createTwitterBeanTest() throws Exception {

        initMocks();
        TwitterBean tb = TwitterBean.createTwitterBean(mockStatus);
        assertEquals(tb.getText(), TEST_TWEET);
        assertEquals(tb.getLanguage(), "en");
        assertEquals(tb.getCreatedDate(), CREATED_DATE);
        assertEquals(tb.getCountryName(), "United Kingdom");
        assertEquals(tb.getFavouriteCount(), 13);
        assertEquals(tb.getRetweetCount(), 123);
        assertEquals(tb.getUsername(), TEST_USER);
        assertEquals(tb.isPossiblySensitive(), false);
        assertEquals(tb.isRetweet(), false);
        List<String> hashTags = new ArrayList<>();
        hashTags.add("#hashtag1");
        hashTags.add("#hashtag2");
        assertEquals(tb.getHashTags(), hashTags);

    }

}
