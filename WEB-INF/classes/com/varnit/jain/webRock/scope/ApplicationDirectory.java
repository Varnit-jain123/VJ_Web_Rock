package com.varnit.jain.webRock.scope;

import java.io.File;

public class ApplicationDirectory {
    private File directory;

    public ApplicationDirectory(String path) {
        this.directory = new File(path);
    }

    public File getDirectory() {
        return directory;
    }
}
