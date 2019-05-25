/*
 * This File is licensed under the MIT License. You should have already received a copy located at LICENSE.txt
 * Copyright 2019 MinorTom <mail in license file>
 */
package net.minortom.davidjumpnrun.configstore;

import java.io.Serializable;
import net.minortom.davidjumpnrun.i18n.Language;

public class Configuration implements Serializable {
    
    // Game Language
    public Language gameLanguage;
    // Online play
    public boolean networkLoggedIn = false;
    public String networkUserId = "";
    public String networkUserToken = "";
    public String networkHost = "";
    // Custom path
    public String customPath = "";
    
}
