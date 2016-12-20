/**
 * This file is part of JATF.
 * <p>
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * <p>
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.io;

import javax.annotation.Nullable;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ClassFile {

    private URI uri;
    private JavaFileObject.Kind kind;

    protected ClassFile(URI uri, JavaFileObject.Kind kind) {
        this.uri = uri;
        this.kind = kind;
    }

    public ClassFile(File classFile) {
        this(classFile.toURI(), JavaFileObject.Kind.CLASS);
    }

    @Nullable
    public byte[] getByteContent() throws IOException {
        if (kind.equals(JavaFileObject.Kind.CLASS)) {
            try (InputStream inputStream = uri.toURL().openStream()) {
                byte[] byteContent = new byte[inputStream.available()];
                if (inputStream.read(byteContent) > 0) {
                    return byteContent;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}
