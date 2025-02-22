package spnegocheck;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Krb5loginconfCheck {
    private static final int MAX_ALLOWED_FILE_LENGTH = 1048576;
    private String fileName = "krb5login.conf";
    private boolean bDashDPresent = false;
    private boolean bFileFound = false;
    private boolean bFileLoaded = false;
    String fileContents = null;
    String jgss_initiateContents = null;
    String jgss_acceptContents = null;
    String jgss_krb5_acceptContents = null;
    String principalName = null;
    String keytab = null;

    public Krb5loginconfCheck() {
        try {
            this.locateAndLoadFile();
        } catch (Exception var2) {
            System.out.println("Exception caught during location and loading of config file");
            var2.printStackTrace();
        }

    }

    private void locateAndLoadFile() throws IOException, Exception {
        String temp = System.getProperty("java.security.auth.login.config");
        if (null != temp && !temp.isEmpty()) {
            this.bDashDPresent = true;
            this.fileName = temp;
        } else {
            this.fileName = System.getenv("DOMAIN_HOME") + "/" + this.fileName;
        }

        File f = new File(this.fileName);
        if (f.exists()) {
            this.bFileFound = true;
            this.fileContents = FileUtils.getFileContents(f);
        }

    }

    public void parseFileContents() throws Exception {
        if (null != this.fileContents && !this.fileContents.isEmpty()) {
            String[] lines = this.fileContents.split("\\r?\\n");
            String thisSection = null;
            String[] arr$ = lines;
            int len$ = lines.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String line = arr$[i$];
                line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    if (line.contains("{")) {
                        if (null != thisSection) {
                            throw new Exception("File not well constructed");
                        }

                        thisSection = line;
                    } else if (line.contains("}")) {
                        if (null == thisSection) {
                            throw new Exception("File not well constructed");
                        }

                        thisSection = thisSection + line;
                        this.parseSection(thisSection);
                        thisSection = null;
                    } else {
                        thisSection = thisSection + line;
                    }
                }
            }

        }
    }

    private void parseSection(String sectionContents) throws Exception {
        System.out.println("Parsing section contents '" + sectionContents + "'");
        String section = sectionContents.substring(0, sectionContents.indexOf("{")).trim();
        System.out.println("Section name: '" + section + "'");
        if (!section.equals("com.sun.security.jgss.initiate") && !section.equals("com.sun.security.jgss.accept") && !section.equals("com.sun.security.jgss.krb5.accept")) {
            throw new Exception("Unexpected section '" + section + "' found in config file");
        } else {
            sectionContents = sectionContents.substring(section.length()).trim();
            if (!sectionContents.endsWith(";")) {
                throw new Exception("Expected section " + section + " to end with a semicolon.");
            } else {
                sectionContents = sectionContents.substring(0, sectionContents.length() - 1).trim();
                if (!sectionContents.startsWith("{")) {
                    throw new Exception("Section " + section + " contents should start with {");
                } else if (!sectionContents.endsWith("}")) {
                    throw new Exception("Section " + section + " contents should start with {");
                } else {
                    sectionContents = sectionContents.substring(1, sectionContents.length() - 1).trim();
                    if (!sectionContents.endsWith(";")) {
                        throw new Exception("Last item in section " + section + " should have a trailing semicolon");
                    } else {
                        sectionContents = sectionContents.substring(0, sectionContents.length() - 1);
                        String requiredLeader = "com.sun.security.auth.module.Krb5LoginModule required";
                        if (!sectionContents.startsWith(requiredLeader)) {
                            throw new Exception("Section " + section + " must start with '" + requiredLeader + "'");
                        } else {
                            sectionContents = sectionContents.substring(requiredLeader.length()).trim();
                            Map nvps = this.getNVPairs(sectionContents);
                            System.out.println("Got back " + nvps.size() + " name/value pairs.");
                            if (!nvps.containsKey("principal")) {
                                throw new Exception("section " + section + " does not contain a setting for principal");
                            } else {
                                String keytab = (String)nvps.get("principal");
                                if (null == this.principalName) {
                                    this.principalName = keytab;
                                } else if (!this.principalName.equals(keytab)) {
                                    throw new Exception("section " + section + " contains a principal setting with value '" + keytab + " but previous sections included a principal of " + this.principalName);
                                }

                                if (!nvps.containsKey("keyTab")) {
                                    throw new Exception("section " + section + " does not contain a setting for keytab");
                                } else {
                                    keytab = (String)nvps.get("keyTab");
                                    if (null == this.keytab) {
                                        this.keytab = keytab;
                                    } else if (!this.keytab.equals(keytab)) {
                                        throw new Exception("section " + section + " contains a keyTab setting with value '" + keytab + " but previous sections included a keyTab setting of " + this.keytab);
                                    }

                                    if (!nvps.containsKey("useKeyTab")) {
                                        throw new Exception("section " + section + " does not contain a setting for useKeyTab");
                                    } else if (!nvps.get("useKeyTab").equals("true")) {
                                        throw new Exception("section " + section + " contain a setting for useKeyTab but the value is not 'true'");
                                    } else if (!nvps.containsKey("storeKey")) {
                                        throw new Exception("section " + section + " does not contain a setting for storeKey");
                                    } else if (!nvps.get("storeKey").equals("true")) {
                                        throw new Exception("section " + section + " contain a setting for storeKey but the value is not 'true'");
                                    } else {
                                        if (!nvps.containsKey("debug")) {
                                            System.out.println("section " + section + " should probably contain a setting for debug=true");
                                        }

                                        System.out.println("Section " + section + " seems OK");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getFilename() {
        return this.fileName;
    }

    public boolean isFileFound() {
        return this.bFileFound;
    }

    private Map<String, String> getNVPairs(String sectionContents) throws Exception {
        System.out.println("Getting next NV pair beginning at '" + sectionContents + "'");
        String s = sectionContents;
        Map<String, String> nvps = new HashMap<String, String>();
        boolean bEqualSeen = false;
        String name = "";
        String value = "";

        for(int i = 0; i < s.length(); ++i) {
            char curChar = s.charAt(i);
            if (!Character.isSpaceChar(curChar) && s.length() != i) {
                if (curChar == '=') {
                    bEqualSeen = true;
                } else if (bEqualSeen) {
                    value = value + curChar;
                } else {
                    name = name + curChar;
                }
            } else if (!name.equals("") || !value.equals("")) {
                if (name.equals("") || value.equals("")) {
                    System.out.println("Error found, name is '" + name + "' value is " + value + "'. Aborting.");
                    throw new Exception("Invalid Name/Value pair beginning at '" + s.substring(i) + "'");
                }

                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                System.out.println("NVPair name: '" + name + "' value: '" + value + "'");
                nvps.put(name, value);
                bEqualSeen = false;
                name = "";
                value = "";
            }
        }

        return nvps;
    }

    public String getPrincipalName() {
        return this.principalName;
    }

    public String getKeytab() {
        return this.keytab;
    }
}
