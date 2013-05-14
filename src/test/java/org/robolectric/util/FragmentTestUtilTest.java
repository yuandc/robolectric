package org.robolectric.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.TestRunners;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.robolectric.util.FragmentTestUtil.startFragment;

@RunWith(TestRunners.WithDefaults.class)
public class FragmentTestUtilTest {
  private TestFragment fragment;

  @Before
  public void setUp() {
    fragment = new TestFragment();
    startFragment(fragment);

    assertThat(fragment.getActivity(), notNullValue());
    assertThat(fragment.getView(), notNullValue());
  }

  @Test
  public void tacos_should_be_found() {
    assertThat(fragment.getView().findViewById(R.id.tacos), notNullValue());
  }

  @Test public void shouldThingy() throws Exception {
    // I'm using 2.0 alpha 2 of Robolectric

    MyFragmentActivity act = new MyFragmentActivity();
    act.onCreate(null);
    Fragment frag = act.getSupportFragmentManager().findFragmentByTag("fragment_tag");
    // The above returns what I expect
    View fragView = frag.getView();
    System.out.println("fragView = " + fragView);
    // The above return null :'-(
    View viewOnFragment = act.findViewById(R.id.button);
    System.out.println("viewOnFragment = " + viewOnFragment);
    // The above also return null :'-(

    // QUESTION: How can I make it so either of the above return a non-null?

    // Note 1: onCreateView() in the Fragment is never called
  }

  public static class MyFragmentActivity extends FragmentActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.fragment_activity);
    }
  }

  public static class TestFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_contents, container, false);
    }
  }
}
