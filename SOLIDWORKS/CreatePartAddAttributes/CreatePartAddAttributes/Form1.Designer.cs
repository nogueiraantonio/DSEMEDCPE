namespace CreatePartAddAttributes
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.m_startSolidworksButton = new System.Windows.Forms.Button();
            this.m_createPartButton = new System.Windows.Forms.Button();
            this.m_createSWCustomPropertyButton = new System.Windows.Forms.Button();
            this.m_importTo3DXButton = new System.Windows.Forms.Button();
            this.m_closeSolidworksButton = new System.Windows.Forms.Button();
            this.m_batchImportFilenameTextBox = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.m_batchImportFilenameButton = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.m_workFolderTextBox = new System.Windows.Forms.TextBox();
            this.m_workFolderButton = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.m_swVersionTextBox = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.m_partNameTextBox = new System.Windows.Forms.TextBox();
            this.m_customPropertyNameTextBox = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.m_customPropertyValueTextBox = new System.Windows.Forms.TextBox();
            this.folderBrowserDialog1 = new System.Windows.Forms.FolderBrowserDialog();
            this.SuspendLayout();
            // 
            // m_startSolidworksButton
            // 
            this.m_startSolidworksButton.Location = new System.Drawing.Point(618, 22);
            this.m_startSolidworksButton.Name = "m_startSolidworksButton";
            this.m_startSolidworksButton.Size = new System.Drawing.Size(197, 42);
            this.m_startSolidworksButton.TabIndex = 0;
            this.m_startSolidworksButton.Text = "1. Start Solidworks";
            this.m_startSolidworksButton.UseVisualStyleBackColor = true;
            this.m_startSolidworksButton.Click += new System.EventHandler(this.m_startSolidworksButton_Click);
            // 
            // m_createPartButton
            // 
            this.m_createPartButton.Location = new System.Drawing.Point(618, 74);
            this.m_createPartButton.Name = "m_createPartButton";
            this.m_createPartButton.Size = new System.Drawing.Size(197, 42);
            this.m_createPartButton.TabIndex = 1;
            this.m_createPartButton.Text = "2. Create Solidworks Part";
            this.m_createPartButton.UseVisualStyleBackColor = true;
            this.m_createPartButton.Click += new System.EventHandler(this.m_createPartButton_Click);
            // 
            // m_createSWCustomPropertyButton
            // 
            this.m_createSWCustomPropertyButton.Location = new System.Drawing.Point(618, 126);
            this.m_createSWCustomPropertyButton.Name = "m_createSWCustomPropertyButton";
            this.m_createSWCustomPropertyButton.Size = new System.Drawing.Size(197, 42);
            this.m_createSWCustomPropertyButton.TabIndex = 2;
            this.m_createSWCustomPropertyButton.Text = "3. Create Part Attributes";
            this.m_createSWCustomPropertyButton.UseVisualStyleBackColor = true;
            this.m_createSWCustomPropertyButton.Click += new System.EventHandler(this.m_createSWCustomPropertyButton_Click);
            // 
            // m_importTo3DXButton
            // 
            this.m_importTo3DXButton.Location = new System.Drawing.Point(618, 178);
            this.m_importTo3DXButton.Name = "m_importTo3DXButton";
            this.m_importTo3DXButton.Size = new System.Drawing.Size(197, 42);
            this.m_importTo3DXButton.TabIndex = 3;
            this.m_importTo3DXButton.Text = "4. Save to 3DEXPERIENCE";
            this.m_importTo3DXButton.UseVisualStyleBackColor = true;
            this.m_importTo3DXButton.Click += new System.EventHandler(this.m_importTo3DXButton_Click);
            // 
            // m_closeSolidworksButton
            // 
            this.m_closeSolidworksButton.Location = new System.Drawing.Point(618, 230);
            this.m_closeSolidworksButton.Name = "m_closeSolidworksButton";
            this.m_closeSolidworksButton.Size = new System.Drawing.Size(197, 42);
            this.m_closeSolidworksButton.TabIndex = 4;
            this.m_closeSolidworksButton.Text = "5. Close Solidworks";
            this.m_closeSolidworksButton.UseVisualStyleBackColor = true;
            this.m_closeSolidworksButton.Click += new System.EventHandler(this.m_closeSolidworksButton_Click);
            // 
            // m_batchImportFilenameTextBox
            // 
            this.m_batchImportFilenameTextBox.Location = new System.Drawing.Point(190, 158);
            this.m_batchImportFilenameTextBox.Name = "m_batchImportFilenameTextBox";
            this.m_batchImportFilenameTextBox.Size = new System.Drawing.Size(292, 22);
            this.m_batchImportFilenameTextBox.TabIndex = 5;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(13, 161);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(166, 17);
            this.label1.TabIndex = 6;
            this.label1.Text = "Solidworks Batch Import :";
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.FileName = "openFileDialog1";
            // 
            // m_batchImportFilenameButton
            // 
            this.m_batchImportFilenameButton.Location = new System.Drawing.Point(488, 153);
            this.m_batchImportFilenameButton.Name = "m_batchImportFilenameButton";
            this.m_batchImportFilenameButton.Size = new System.Drawing.Size(84, 33);
            this.m_batchImportFilenameButton.TabIndex = 7;
            this.m_batchImportFilenameButton.Text = "Browse...";
            this.m_batchImportFilenameButton.UseVisualStyleBackColor = true;
            this.m_batchImportFilenameButton.Click += new System.EventHandler(this.button6_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(15, 205);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(164, 17);
            this.label2.TabIndex = 8;
            this.label2.Text = "Solidworks Work Folder :";
            // 
            // m_workFolderTextBox
            // 
            this.m_workFolderTextBox.Location = new System.Drawing.Point(190, 205);
            this.m_workFolderTextBox.Name = "m_workFolderTextBox";
            this.m_workFolderTextBox.Size = new System.Drawing.Size(292, 22);
            this.m_workFolderTextBox.TabIndex = 9;
            // 
            // m_workFolderButton
            // 
            this.m_workFolderButton.Location = new System.Drawing.Point(488, 202);
            this.m_workFolderButton.Name = "m_workFolderButton";
            this.m_workFolderButton.Size = new System.Drawing.Size(84, 33);
            this.m_workFolderButton.TabIndex = 10;
            this.m_workFolderButton.Text = "Browse...";
            this.m_workFolderButton.UseVisualStyleBackColor = true;
            this.m_workFolderButton.Click += new System.EventHandler(this.m_workFolderButton_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(44, 252);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(135, 17);
            this.label3.TabIndex = 11;
            this.label3.Text = "Solidworks Version :";
            // 
            // m_swVersionTextBox
            // 
            this.m_swVersionTextBox.Location = new System.Drawing.Point(190, 252);
            this.m_swVersionTextBox.Name = "m_swVersionTextBox";
            this.m_swVersionTextBox.Size = new System.Drawing.Size(292, 22);
            this.m_swVersionTextBox.TabIndex = 12;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(96, 22);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(83, 17);
            this.label4.TabIndex = 13;
            this.label4.Text = "Part Name :";
            // 
            // m_partNameTextBox
            // 
            this.m_partNameTextBox.Location = new System.Drawing.Point(190, 17);
            this.m_partNameTextBox.Name = "m_partNameTextBox";
            this.m_partNameTextBox.Size = new System.Drawing.Size(292, 22);
            this.m_partNameTextBox.TabIndex = 14;
            // 
            // m_customPropertyNameTextBox
            // 
            this.m_customPropertyNameTextBox.Location = new System.Drawing.Point(190, 64);
            this.m_customPropertyNameTextBox.Name = "m_customPropertyNameTextBox";
            this.m_customPropertyNameTextBox.Size = new System.Drawing.Size(292, 22);
            this.m_customPropertyNameTextBox.TabIndex = 16;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(17, 69);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(162, 17);
            this.label5.TabIndex = 15;
            this.label5.Text = "Custom Property Name :";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(18, 114);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(161, 17);
            this.label6.TabIndex = 17;
            this.label6.Text = "Custom Property Value :";
            // 
            // m_customPropertyValueTextBox
            // 
            this.m_customPropertyValueTextBox.Location = new System.Drawing.Point(190, 111);
            this.m_customPropertyValueTextBox.Name = "m_customPropertyValueTextBox";
            this.m_customPropertyValueTextBox.Size = new System.Drawing.Size(292, 22);
            this.m_customPropertyValueTextBox.TabIndex = 18;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(850, 318);
            this.Controls.Add(this.m_customPropertyValueTextBox);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.m_customPropertyNameTextBox);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.m_partNameTextBox);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.m_swVersionTextBox);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.m_workFolderButton);
            this.Controls.Add(this.m_workFolderTextBox);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.m_batchImportFilenameButton);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.m_batchImportFilenameTextBox);
            this.Controls.Add(this.m_closeSolidworksButton);
            this.Controls.Add(this.m_importTo3DXButton);
            this.Controls.Add(this.m_createSWCustomPropertyButton);
            this.Controls.Add(this.m_createPartButton);
            this.Controls.Add(this.m_startSolidworksButton);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button m_startSolidworksButton;
        private System.Windows.Forms.Button m_createPartButton;
        private System.Windows.Forms.Button m_createSWCustomPropertyButton;
        private System.Windows.Forms.Button m_importTo3DXButton;
        private System.Windows.Forms.Button m_closeSolidworksButton;
        private System.Windows.Forms.TextBox m_batchImportFilenameTextBox;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.Windows.Forms.Button m_batchImportFilenameButton;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox m_workFolderTextBox;
        private System.Windows.Forms.Button m_workFolderButton;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox m_swVersionTextBox;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox m_partNameTextBox;
        private System.Windows.Forms.TextBox m_customPropertyNameTextBox;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.TextBox m_customPropertyValueTextBox;
        private System.Windows.Forms.FolderBrowserDialog folderBrowserDialog1;
    }
}

