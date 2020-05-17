 void readTriggers() {
        try {
            ctx = Util.checkContext(ctx);
            Util.clearTable(tblTriggers);
            String typerelationship = getListBoxValueName();
            String result = "", derived = "";
            if (rbTypeMode.isSelected()) {
                result = Util.execMQL(ctx, "print type '" + typerelationship + "' select trigger dump");
                derived = Util.execMQL(ctx, "print type '" + typerelationship + "' select derived.trigger dump");
            } else if (rbRelationshipMode.isSelected()) {
                result = Util.execMQL(ctx, "print relationship '" + typerelationship + "' select trigger dump");
                derived = Util.execMQL(ctx, "print relationship '" + typerelationship + "' select derived.trigger dump");
            }
            if (result.isEmpty())
                return;
            DefaultTableModel model = (DefaultTableModel) tblTriggers.getModel();
            String[] a = result.split(",");
            Arrays.sort(a, nameComp);
            for (int i = 0; i < a.length; i++) {
                String[] b = a[i].split(":");
                String prog = b[1].substring(0, b[1].indexOf("("));
                String param = b[1].substring(b[1].indexOf("(") + 1, b[1].indexOf(")"));
                String[] params;
                if (prog.equals("emxTriggerManager")) {
                    prog = "TM";
                    params = param.split(" ");
                } else {
                    params = new String[1];
                    params[0] = param;
                }
                for (int j = 0; j < params.length; j++) {
                    if (params[j].trim().isEmpty())
                        continue;
                    Object[] row = new Object[5];
                    //AN
                    String an_triggerType = "";
                    if (b[0].endsWith("Check")) {
                        row[0] = b[0].substring(0, b[0].length() - 5);
                        row[2] = prog + ": " + params[j];
                        //AN
                        an_triggerType = "check";
                    } else if (b[0].endsWith("Action")) {
                        row[0] = b[0].substring(0, b[0].length() - 6);
                        row[4] = prog + ": " + params[j];
                        //AN
                        an_triggerType = "action";
                    } else if (b[0].endsWith("Override")) {
                        row[0] = b[0].substring(0, b[0].length() - 8);
                        row[3] = prog + ": " + params[j];
                        //AN
                        an_triggerType = "override";
                    }
                    row[1] = derived.matches(".*" + b[0] + ":.*[^\\w]" + params[j] + "[^\\w].*");

                    //AN  -- start

                    String OUT_FILENAME = "C:\\tmp\\script.txt";
                    String an_trigger = (String)row[0];

                    boolean an_isDerivedTrigger = (boolean)row[1];

                    String an_script = "";
                    if (!an_isDerivedTrigger) // not derived
                    {
                        if (rbTypeMode.isSelected()) {
                            an_script = an_GetTriggerScript("type", typerelationship, an_trigger, an_triggerType, params);
                        } else if (rbRelationshipMode.isSelected()) {
                            an_script = an_GetTriggerScript("relationship", typerelationship, an_trigger, an_triggerType, params);
                        }
                        try {
                            BufferedWriter out = new
                                    BufferedWriter(new FileWriter(OUT_FILENAME, true));
                            out.write(an_script);
                            out.close();
                        }
                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    //AN  -- end
                    model.addRow(row);
                }
            }
            tblTriggers.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblTriggers.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblTriggers.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblTriggers.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblTriggers.getColumnModel().getColumn(4).setPreferredWidth(200);
            tblTriggers.scrollRectToVisible(tblTriggers.getCellRect(0, 0, true));
        } catch (MatrixException ex) {
            Util.handleMatrixException(ex);
        }
    }
        //AN - begin
        String an_GetTriggerScript(String an_mqltype, String an_typerelationship, String an_trigger, String an_triggerType, String params[])
    {
        String an_triggerScript = "";
        // an_triggerScript += "mod " + an_mqltype + " '" + an_typerelationship + "' remove trigger " + an_trigger + " " + an_triggerType + ";\n" ;

        if (params.length > 0 ) {
            an_triggerScript += "mod " + an_mqltype + " '" + an_typerelationship + "' add trigger " + an_trigger + " " + an_triggerType + " emxTriggerManager input ";
            an_triggerScript += "'";
            for (int k = 0; k < params.length; k++) {
                an_triggerScript += params[k];
                if (k-params.length != -1)
                {
                    an_triggerScript += " ";
                }
            }
            an_triggerScript += "';";
        }
        return an_triggerScript;
    }

    String an_GetPolicyTriggerScript(String an_mqltype, String policy,String state,String an_trigger, String an_triggerType, String params[])
    {
        String an_triggerScript = "";
        // an_triggerScript += "mod " + an_mqltype + " '" + an_typerelationship + "' remove trigger " + an_trigger + " " + an_triggerType + ";\n" ;

        if (params.length > 0 ) {
            an_triggerScript += "mod " + an_mqltype + " '" + policy + "' state[" + state + "] add trigger " + an_trigger + " " + an_triggerType + " emxTriggerManager input ";
            an_triggerScript += "'";
            for (int k = 0; k < params.length; k++) {
                an_triggerScript += params[k];
                if (k-params.length != -1)
                {
                    an_triggerScript += " ";
                }
            }
            an_triggerScript += "';\n";
        }
        return an_triggerScript;
    }
//AN - end

    String[] getAllPoliciesForType() {
        try {
            String type = getListBoxValueName();
            BusinessType bt = new BusinessType(type, ctx.getVault());
            PolicyList pl = bt.getPolicies(ctx);
            PolicyItr itr = new PolicyItr(pl);
            ArrayList<String> al = new ArrayList<String>();
            while (itr.next())
                al.add(itr.obj().getName());
            String[] allPoliciesForType = al.toArray(new String[al.size()]);
            Arrays.sort(allPoliciesForType, nameComp);
            return allPoliciesForType;
        } catch (MatrixException ex) {
            Util.handleMatrixException(ex);
        }
        return null;
    }

    void readPolicyTriggers() {
        try {
            ctx = Util.checkContext(ctx);
            Util.clearTable(tblPolicyTriggers);
            String[] allPoliciesForType = getAllPoliciesForType();
            /* Loop over all policies for the types in hierarchy and fetch state and trigger information */
            DefaultTableModel model = (DefaultTableModel) tblPolicyTriggers.getModel();
            for (int p = 0; p < allPoliciesForType.length; p++) {
                String policy = allPoliciesForType[p];
//                System.out.println(policy);
                String result = Util.execMQL(ctx, "print policy '" + policy + "' select state dump");
                if (result.isEmpty())
                    continue;
                String[] states = result.split(",");
                for (int s = 0; s < states.length; s++) {
                    String state = states[s];
//                    System.out.println(state);
                    result = Util.execMQL(ctx, "print policy '" + policy + "' select state[" + state + "].trigger dump");
                    if (result.isEmpty()) {
                        String[] row = new String[6];
                        row[0] = policy;
                        row[1] = state;
                        model.addRow(row);
                        continue;
                    }
                    String[] a = result.split(",");
                    Arrays.sort(a, nameComp);
                    for (int i = 0; i < a.length; i++) {
                        String[] b = a[i].split(":");
                        String prog = b[1].substring(0, b[1].indexOf("("));
                        String param = b[1].substring(b[1].indexOf("(") + 1, b[1].indexOf(")"));
                        String[] params;
                        if (prog.equals("emxTriggerManager")) {
                            prog = "TM";
                            params = param.split(" ");
                        } else {
                            params = new String[1];
                            params[0] = param;
                        }
                        for (int j = 0; j < params.length; j++) {
                            String[] row = new String[6];
                            row[0] = policy;
                            row[1] = state;
                            //AN
                            String an_triggerType = "";
                            if (b[0].endsWith("Check")) {
                                row[2] = b[0].substring(0, b[0].length() - 5);
                                row[3] = prog + ": " + params[j];
                                an_triggerType = "check";
                            } else if (b[0].endsWith("Action")) {
                                row[2] = b[0].substring(0, b[0].length() - 6);
                                row[5] = prog + ": " + params[j];
                                an_triggerType = "action";
                            } else if (b[0].endsWith("Override")) {
                                row[2] = b[0].substring(0, b[0].length() - 8);
                                row[4] = prog + ": " + params[j];
                                an_triggerType = "override";
                            }

                            //AN  -- start

                            String OUT_FILENAME = "C:\\tmp\\policy.script.txt";
                            String an_trigger = (String)row[2].toLowerCase();

                            String an_script =
                                    an_GetPolicyTriggerScript("policy", policy, state, an_trigger, an_triggerType, params);
                            try {
                                BufferedWriter out = new
                                        BufferedWriter(new FileWriter(OUT_FILENAME, true));
                                out.write(an_script);
                                out.close();
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }

                            //AN  -- end


                            model.addRow(row);
                        }
                    }
                }
            }
            tblPolicyTriggers.scrollRectToVisible(tblPolicyTriggers.getCellRect(0, 0, true));
        } catch (MatrixException ex) {
            Util.handleMatrixException(ex);
        }
    }
