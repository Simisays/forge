/*
 * Forge: Play Magic: the Gathering.
 * Copyright (C) 2011  Forge Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package forge.gui.download;

import forge.localinstance.properties.ForgeConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.nio.file.Files;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GuiDownloadAdventureMusic extends GuiDownloadService {
    public String getTitle() {
        return "Download Adventure Soundtrack";
    }

    @Override
    protected final Map<String, String> getNeededFiles() {
        // Download music.zip and extract it to subfolders
        final Map<String, String> download = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
          addMissingItems(download, ForgeConstants.ADVENTURE_CUSTOMMUSIC_FILE ,     ForgeConstants.ADVENTURE_COMMON_CUSTOM_CARD_PICS_DIR);
        return download;
    }
    // Method to unzip a zip file to a target directory
    protected void unzipFile(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // If the entry is a file, extract it
                    File outFile = new File(filePath);
                    // Ensure parent directories exist
                    outFile.getParentFile().mkdirs();
                    Files.copy(zipIn, outFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    } else {
                    // If the entry is a directory, create it
                    new File(filePath).mkdirs();
                }
                zipIn.closeEntry();
            }
        }
    }
    // Call this method after download completes
    @Override
    protected void onDownloadComplete() {
        super.onDownloadComplete();
        String zipPath = ForgeConstants.ADVENTURE_CUSTOMMUSIC_FILE;
        String targetDir = ForgeConstants.ADVENTURE_COMMON_CUSTOM_CARD_PICS_DIR;
        try {
            unzipFile(zipPath, targetDir);
        } catch (IOException e) {
            e.printStackTrace();
            // handle error if needed
        }
    }
}

