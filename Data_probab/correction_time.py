import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import os
from pathlib import Path
import io

def process_nested_folders(root_directory):
    # 1. Use pathlib.rglob to recursively find all .csv files in all subfolders
    print(f"Scanning directory and subfolders in: {root_directory}")
    csv_files = list(Path(root_directory).rglob("*.csv"))
    
    if not csv_files:
        print("No CSV files found in the provided directory or its subfolders.")
        return

    print(f"Found {len(csv_files)} CSV files. Extracting data...")

    all_correction_times = []
    processed_count = 0

    for file_path in csv_files:
        try:
            # 2. Read the file and isolate the KEYSTROKE LOG
            with open(file_path, 'r', encoding='utf-8') as f:
                lines = f.readlines()

            log_lines = []
            start_reading = False
            for line in lines:
                if line.strip() == "=== KEYSTROKE LOG ===":
                    start_reading = True
                    continue
                if line.strip() == "=== SUMMARY ===":
                    break
                if start_reading and line.strip() != "":
                    log_lines.append(line)

            # Skip file if no keystroke log was found
            if not log_lines:
                continue

            # 3. Read directly into pandas using StringIO (faster, no temp files needed)
            csv_data = "".join(log_lines)
            df = pd.read_csv(io.StringIO(csv_data))
            
            df['elapsed_ms'] = pd.to_numeric(df['elapsed_ms'], errors='coerce')

            # 4. Find Backspace indices and calculate (t_n - t_b)
            if 'key' in df.columns and 'elapsed_ms' in df.columns:
                backspace_indices = df[df['key'] == 'Backspace'].index

                for idx in backspace_indices:
                    if idx + 1 < len(df):
                        t_b = df.loc[idx, 'elapsed_ms']
                        t_n = df.loc[idx + 1, 'elapsed_ms']
                        val = t_n - t_b
                        
                        if not np.isnan(val):
                            all_correction_times.append(val)
                
                processed_count += 1
                        
        except Exception as e:
            print(f"Error processing {file_path.name}: {e}")

    print(f"\nSuccessfully processed {processed_count} files.")
    print(f"Total correction times extracted: {len(all_correction_times)}")

    # 5. Plot the combined Probability Density Function
    if len(all_correction_times) > 0:
        plt.figure(figsize=(10, 6))
        
        # KDE plot for the aggregated data
        sns.kdeplot(all_correction_times, fill=True, color="purple", bw_adjust=1, alpha=0.4)
        sns.rugplot(all_correction_times, color="black", alpha=0.3, height=0.05)
        
        # Overlay a histogram to show the actual distribution
        sns.histplot(all_correction_times, stat="density", bins=30, color="purple", alpha=0.2)
        
        plt.xlabel("Correction Time ($t_n - t_b$) [ms]", fontsize=12)
        plt.ylabel("Density", fontsize=12)
        plt.title(f"Aggregated PDF of Correction Time\n(From {processed_count} files across folders)", fontsize=14)
        plt.grid(True, alpha=0.3)
        plt.tight_layout()
        
        output_image = "aggregated_correction_time_pdf.png"
        plt.savefig(output_image, dpi=300)
        plt.show()
        print(f"Plot successfully generated and saved to the current folder as '{output_image}'.")
    else:
        print("Not enough Backspace data found across all files to generate a plot.")

# --- RUN THE SCRIPT ---
# Change "." to the main folder path where all your subfolders are located.
# For example: root_folder = "C:/Users/Name/Documents/TypecraftData"
root_folder = "." 
process_nested_folders(root_folder) 