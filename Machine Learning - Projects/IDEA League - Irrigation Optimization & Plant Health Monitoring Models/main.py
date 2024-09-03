from data_prep import generate_dummy_data
from et_model import compute_ET
from indiv_irrigation_model import train_irrigation_model
from clustering_model import perform_clustering
from health_model import train_health_model, correlation_analysis, plot_feature_importances

import numpy as np
import pandas as pd

# Data Preparation
df, df_soil, df_combined, df_plant_health = generate_dummy_data()

# Compute ET using Penman-Monteith
df = compute_ET(df, method='Penman-Monteith')
print(df['ETc'].head())

# Combine soil data with ET0, ETc, and plant health data
df_combined = pd.concat([df_soil, df[['ET0', 'ETc']], df_plant_health], axis=1)
df_combined['optimal_irrigation'] = np.random.uniform(3000000, 5000000, len(df_combined))  # Optimal irrigation in mm/day

# Train and evaluate irrigation model
# model, y_pred = train_irrigation_model(df_combined)

# Define features for clustering
features = ['soil_moisture', 'soil_temperature', 'soil_pH', 'nutrient_levels', 'soil_salinity', 'ETc']

# Perform clustering and analysis
#perform_clustering(df_combined, features)

def get_health_analysis():

    # Train and evaluate health model
    models, feature_importance_df = train_health_model(df_combined)
    print(feature_importance_df)
    plot_feature_importances(feature_importance_df)

    # Print correlation analysis of the soil and plant health measurements 
    print(correlation_analysis(df_combined))
    
get_health_analysis()