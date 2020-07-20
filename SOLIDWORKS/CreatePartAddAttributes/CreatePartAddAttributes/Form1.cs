using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using SldWorks;
using SwConst;

namespace CreatePartAddAttributes
{
    public partial class Form1 : Form
    {
        SldWorks.SldWorks m_swApp = null;
        private const string FILETYPE = ".SLDPRT";

        public Form1()
        {
            InitializeComponent();
        }
        private void Form1_Load(object sender, EventArgs e)
        {
            SWBatchImportFilename = System.Configuration.ConfigurationManager.AppSettings["SWBatchImportFilename"];
            SWWorkFolder = System.Configuration.ConfigurationManager.AppSettings["SWWorkFolder"];
            SWVersion = System.Configuration.ConfigurationManager.AppSettings["SWVersion"];

            PartName = System.Configuration.ConfigurationManager.AppSettings["PartName"];
            CustomPropertyName = System.Configuration.ConfigurationManager.AppSettings["CustomPropertyName"];
            CustomPropertyValue = System.Configuration.ConfigurationManager.AppSettings["CustomPropertyValue"];
        }

        public string SWBatchImportFilename
        {
            get
            {
                return m_batchImportFilenameTextBox.Text;
            }

            private set
            {
                m_batchImportFilenameTextBox.Text = value;
            }
        }
        public string SWWorkFolder
        {
            get
            {
                return m_workFolderTextBox.Text;
            }

            private set
            {
                m_workFolderTextBox.Text = value;
            }
        }
        public string SWVersion
        {
            get
            {
                return m_swVersionTextBox.Text;
            }

            private set
            {
                m_swVersionTextBox.Text = value;
            }
        }
        public string PartName
        {
            get
            {
                return m_partNameTextBox.Text;
            }

            private set
            {
                m_partNameTextBox.Text = value;
            }
        }
        public string CustomPropertyName
        {
            get
            {
                return m_customPropertyNameTextBox.Text;
            }

            private set
            {
                m_customPropertyNameTextBox.Text = value;
            }
        }
        public string CustomPropertyValue
        {
            get
            {
                return m_customPropertyValueTextBox.Text;
            }

            private set
            {
                m_customPropertyValueTextBox.Text = value;
            }
        }

        private void m_startSolidworksButton_Click(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            try
            {
                m_swApp = new SldWorks.SldWorks();
                m_swApp.Visible = true;

            }
            catch
            { }
            finally
            {
                this.Cursor = Cursors.Default;
            }
        }

        private void m_createPartButton_Click(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            try
            {
                string partPath = m_swApp.GetUserPreferenceStringValue((int)swUserPreferenceStringValue_e.swDefaultTemplatePart);

                PartDoc partDoc = null;

                if ((null == partPath) || (!System.IO.File.Exists(partPath)))
                {
                    partDoc = m_swApp.NewPart();
                }
                else
                {
                    partDoc = m_swApp.NewDocument(partPath, 0, 0, 0);
                }
            }
            catch
            {
            }
            finally
            {
                this.Cursor = Cursors.Default;
            }
        }

        private void m_createSWCustomPropertyButton_Click(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            try
            {
                ModelDoc2 swModel = m_swApp.ActiveDoc;

                ModelDocExtension swModelDocExt = swModel.Extension;

                CustomPropertyManager swCustProp = swModelDocExt.CustomPropertyManager["Default"];

                swCustProp.Add3(CustomPropertyName, ((int)swCustomInfoType_e.swCustomInfoText), CustomPropertyValue, ((int)swCustomPropertyAddOption_e.swCustomPropertyReplaceValue));
            }
            catch
            {
            }
            finally
            {
                this.Cursor = Cursors.Default;
            }
        }

        private void m_importTo3DXButton_Click(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            try
            {
                string swWorkFolder = SWWorkFolder;
                if (!swWorkFolder.EndsWith(System.IO.Path.DirectorySeparatorChar.ToString()))
                {
                    swWorkFolder += System.IO.Path.DirectorySeparatorChar;
                }
                string file = string.Format("{0}{1}{2}", swWorkFolder, PartName, FILETYPE);

                ((ModelDoc2)m_swApp.ActiveDoc).SaveAs(file);

                m_swApp.CloseAllDocuments(true);

                Process process = new Process();
                // Configure the process using the StartInfo properties.
                process.StartInfo.FileName = SWBatchImportFilename;
                process.StartInfo.Arguments = string.Format("--file \"{0}\" --swversion {1}", file, SWVersion);
                process.StartInfo.WindowStyle = ProcessWindowStyle.Normal;
                process.Start();
                process.WaitForExit();// Waits here for the process to exit
            }
            catch
            {
            }
            finally
            {
                this.Cursor = Cursors.Default;
            }
        }


        private void m_closeSolidworksButton_Click(object sender, EventArgs e)
        {
            this.Cursor = Cursors.WaitCursor;

            try
            {
                m_swApp.ExitApp();
                m_swApp = null;
            }
            catch
            {
            }
            finally
            {
                this.Cursor = Cursors.Default;
            }
        }


        private void button6_Click(object sender, EventArgs e)
        {
            openFileDialog1.CheckFileExists = true;
            openFileDialog1.CheckPathExists = true;

            openFileDialog1.Filter = "SaveInto3DEXPERIENCE file | SaveInto3DEXPERIENCE.exe";
            if (DialogResult.OK == openFileDialog1.ShowDialog())
            {
                SWBatchImportFilename = openFileDialog1.FileName;
            }
        }

        private void m_workFolderButton_Click(object sender, EventArgs e)
        {
            if (DialogResult.OK == folderBrowserDialog1.ShowDialog())
            {
                SWWorkFolder = folderBrowserDialog1.SelectedPath;
            }
        }
    }
}
