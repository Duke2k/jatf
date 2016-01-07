/**
 * This file is part of JATF.
 *
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.io;

import javax.annotation.Nullable;
import javax.tools.SimpleJavaFileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class SourceFile extends SimpleJavaFileObject {

    protected SourceFile(URI uri, Kind kind) {
        super(uri, kind);
    }

    public SourceFile(File sourceFile) {
        this(sourceFile.toURI(), Kind.SOURCE);
    }

    @Override
    @Nullable
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = uri.toURL().openStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        }
        CharSequence result = stringBuilder.toString();
        return result.length() == 0 ? null : result;
    }
}
