package org.robolectric.shadows;

import android.content.res.BridgeResources;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.layoutlib.bridge.Bridge;
import com.android.resources.ResourceType;
import org.robolectric.internal.Implements;
import org.robolectric.internal.RealObject;
import org.robolectric.res.ResName;

import static org.fest.reflect.core.Reflection.field;

@Implements(BridgeResources.class)
public class ShadowBridgeResources extends ShadowResources {
    @RealObject private BridgeResources realBridgeResources;
    private IProjectCallback projectCallback;

    @Override public int getIdentifier(String name, String defType, String defPackage) {
        ResName qualifiedName = ResName.qualifyResName(name, defPackage, defType);
        ResourceType type = ResourceType.getEnum(qualifiedName.type);
        Integer resourceId = qualifiedName.isSystem()
                ? Bridge.getResourceId(type, qualifiedName.name)
                : getProjectCallback().getResourceId(type, qualifiedName.name);
        return resourceId == null ? 0 : resourceId;
    }

    public IProjectCallback getProjectCallback() {
        if (projectCallback == null) {
            projectCallback = field("mProjectCallback").ofType(IProjectCallback.class).in(realResources).get();
        }
        return projectCallback;
    }
}
