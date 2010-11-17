package com.thoughtworks.blipit;

import java.io.IOException;

public interface TransportLayer {

    public String getBlipTitle() throws IOException;

    public String getBlipSnippet() throws IOException;
    
}
