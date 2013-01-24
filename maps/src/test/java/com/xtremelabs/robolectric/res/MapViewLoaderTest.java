package com.xtremelabs.robolectric.res;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.google.android.maps.MapView;
import com.xtremelabs.robolectric.WithDefaults;
import com.xtremelabs.robolectric.maps.R;
import com.xtremelabs.robolectric.tester.android.util.Attribute;
import com.xtremelabs.robolectric.tester.android.util.ResName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(WithDefaults.class)
public class MapViewLoaderTest {
    private Activity context;

    @Before
    public void setUp() throws Exception {
        context = new Activity() {};
    }

    @Test
    public void testMapView() throws Exception {
        RelativeLayout mainView = (RelativeLayout) inflate("mapview");
        assertThat(mainView.findViewById(R.id.map_view), instanceOf(MapView.class));
    }

    private View inflate(String packageName, String layoutName, String qualifiers) {
        return inflate(context, packageName, layoutName, null, qualifiers);
    }

    public View inflate(Context context, String packageName, String key, ViewGroup parent, String qualifiers) {
        ResourceLoader resourceLoader = shadowOf(context.getResources()).getResourceLoader();
        return new RoboLayoutInflater(resourceLoader).inflateView(context, new ResName(packageName + ":layout/" + key),
                new ArrayList<Attribute>(), parent, qualifiers);
    }

    private View inflate(String layoutName) {
        return inflate(layoutName, "");
    }

    private View inflate(String layoutName, String qualifiers) {
        return inflate(R.class.getPackage().getName(), layoutName, qualifiers);
    }
}
