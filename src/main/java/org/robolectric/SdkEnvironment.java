package org.robolectric;

import com.android.ide.common.resources.FrameworkResources;
import com.android.ide.common.resources.ResourceRepository;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.io.IAbstractResource;
import com.android.io.StreamException;
import org.robolectric.bytecode.ClassHandler;
import org.robolectric.bytecode.ShadowMap;
import org.robolectric.bytecode.ShadowWrangler;
import org.robolectric.res.Fs;
import org.robolectric.res.FsFile;
import org.robolectric.res.PackageResourceLoader;
import org.robolectric.res.ResourceExtractor;
import org.robolectric.res.ResourceIndex;
import org.robolectric.res.ResourceLoader;
import org.robolectric.res.ResourcePath;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SdkEnvironment {
    private final SdkConfig sdkConfig;
    private final ClassLoader robolectricClassLoader;
    public final Map<ShadowMap, ShadowWrangler> classHandlersByShadowMap = new HashMap<ShadowMap, ShadowWrangler>();
    private ClassHandler currentClassHandler;
    private ResourceLoader systemResourceLoader;

    public SdkEnvironment(SdkConfig sdkConfig, ClassLoader robolectricClassLoader) {
        this.sdkConfig = sdkConfig;
        this.robolectricClassLoader = robolectricClassLoader;
    }

    public ResourceLoader createSystemResourceLoader(MavenCentral mavenCentral, RobolectricTestRunner robolectricTestRunner) {
        URL url = mavenCentral.getLocalArtifactUrl(robolectricTestRunner, sdkConfig.getSystemResourceDependency());
        Fs systemResFs = Fs.fromJar(url);
        ResourceExtractor resourceIndex = new ResourceExtractor(getRobolectricClassLoader());
        ResourcePath resourcePath = new ResourcePath(resourceIndex.getProcessedRFile(), systemResFs.join("res"), systemResFs.join("assets"));
        return new SystemPackageResourceLoader(resourcePath, resourceIndex);
    }

    public synchronized ResourceLoader getSystemResourceLoader(MavenCentral mavenCentral, RobolectricTestRunner robolectricTestRunner) {
        if (systemResourceLoader == null) {
            systemResourceLoader = createSystemResourceLoader(mavenCentral, robolectricTestRunner);
        }
        return systemResourceLoader;
    }

    public Class<?> bootstrappedClass(Class<?> testClass) {
        try {
            return robolectricClassLoader.loadClass(testClass.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ClassLoader getRobolectricClassLoader() {
        return robolectricClassLoader;
    }

    /**
     * @deprecated use {@link org.robolectric.Robolectric.Reflection#setFinalStaticField(Class, String, Object)}
     */
    public static void setStaticValue(Class<?> clazz, String fieldName, Object value) {
        Robolectric.Reflection.setFinalStaticField(clazz, fieldName, value);
    }

    public ClassHandler getCurrentClassHandler() {
        return currentClassHandler;
    }

    public void setCurrentClassHandler(ClassHandler currentClassHandler) {
        this.currentClassHandler = currentClassHandler;
    }

    public interface Factory {
        public SdkEnvironment create();
    }

    public class SystemPackageResourceLoader extends PackageResourceLoader {
        private final ResourceRepository resourceRepository;

        public SystemPackageResourceLoader(ResourcePath resourcePath, ResourceIndex resourceIndex) {
            super(resourcePath, resourceIndex);

            ResourceRepository resourceRepository = new FrameworkResources();
            try {
                resourceRepository.loadResources(new FsFileRes(resourcePath.resourceBase));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.resourceRepository = resourceRepository;
        }

        public ResourceRepository getResourceRepository() {
            return resourceRepository;
        }

        private class FsFileRes implements IAbstractFolder, IAbstractFile {
            private final FsFile fsFile;

            public FsFileRes(FsFile fsFile) {
                this.fsFile = fsFile;
            }

            @Override public boolean hasFile(String name) {
                return fsFile.join(name).exists();
            }

            @Override public IAbstractFile getFile(String name) {
                return new FsFileRes(fsFile.join(name));
            }

            @Override public IAbstractFolder getFolder(String name) {
                return new FsFileRes(fsFile.join(name));
            }

            @Override public IAbstractResource[] listMembers() {
                FsFile[] fsFiles = fsFile.listFiles();
                IAbstractResource[] members = new IAbstractResource[fsFiles.length];
                for (int i = 0; i < fsFiles.length; i++) {
                    members[i] = new FsFileRes(fsFiles[i]);
                }
                return members;
            }

            @Override public String[] list(FilenameFilter filter) {
                FsFile[] fsFiles = fsFile.listFiles();
                String[] members = new String[fsFiles.length];
                for (int i = 0; i < fsFiles.length; i++) {
                    members[i] = fsFiles[i].getName();
                }
                return members;
            }

            @Override public String getName() {
                return fsFile.getName();
            }

            @Override public String getOsLocation() {
                return fsFile.getPath();
            }

            @Override public boolean exists() {
                return fsFile.exists();
            }

            @Override public IAbstractFolder getParentFolder() {
                return new FsFileRes(fsFile.getParent());
            }

            @Override public boolean delete() {
                throw new UnsupportedOperationException();
            }

            @Override public InputStream getContents() throws StreamException {
                try {
                    return fsFile.getInputStream();
                } catch (IOException e) {
                    throw new StreamException(e, this);
                }
            }

            @Override public void setContents(InputStream source) throws StreamException {
                throw new UnsupportedOperationException();
            }

            @Override public OutputStream getOutputStream() throws StreamException {
                throw new UnsupportedOperationException();
            }

            @Override public PreferredWriteMode getPreferredWriteMode() {
                return null;
            }

            @Override public long getModificationStamp() {
                return 0;
            }
        }
    }
}
