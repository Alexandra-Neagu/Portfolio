import numpy as np
import pandas as pd

def generate_dummy_data(rand_seed=42, n=1000):
    np.random.seed(rand_seed)

    # Generate random (dummy) data for soil measurements, weather conditions, air measurements, and plant-specific factors
    data = {
        # weather measurements
        'precipitation': np.random.uniform(0, 20, n),  # Precipitation in mm
        'solar_radiation': np.random.uniform(0, 30, n),  # Solar radiation in MJ/m2/day
        'wind_speed': np.random.uniform(0, 10, n),  # Wind speed in m/s
        # air measurements
        'relative_humidity': np.random.uniform(20, 90, n),  # Relative humidity in %
        'ambient_temperature': np.random.uniform(15, 35, n),  # Ambient temperature in Celsius
        'dew_point': np.random.uniform(5, 20, n),  # Dew point in Celsius
        # plant-specific factors
        'transpiration_rate': np.random.uniform(0.1, 10, n),  # Transpiration rate in mm/day
        'crop_water_stress_index': np.random.uniform(0, 1, n),  # Crop water stress index (0-1)
        'crop_coefficient': np.random.uniform(0.7, 1.2, n)  # Crop coefficient (Kc)
    }
    # Generate random (dummy) data for soil measurements
    data_soil = {
        'soil_moisture': np.random.uniform(10, 60, n),  # Soil moisture in %
        'soil_temperature': np.random.uniform(10, 30, n),  # Soil temperature in Celsius
        'soil_pH': np.random.uniform(5, 8, n),  # Soil pH
        'nutrient_levels': np.random.uniform(0, 100, n),  # Nutrient levels in mg/kg
        'soil_salinity': np.random.uniform(0, 10, n)  # Soil salinity in dS/m
    }
    # Generate random (dummy) data for plant health metrics
    data_plant_health = {
        'plant_height': np.random.uniform(10, 100, n),  # Plant height in cm
        'survival_rate': np.random.uniform(0.5, 1, n),  # Survival rate (0-1)
        'trunk_girth': np.random.uniform(1, 10, n)  # Trunk girth in cm
    }

    # Create DataFrames
    df = pd.DataFrame(data)
    df_soil = pd.DataFrame(data_soil)
    df_plant_health = pd.DataFrame(data_plant_health)

    # Combine soil data with weather data
    df_combined = pd.concat([df_soil, df[['transpiration_rate', 'crop_water_stress_index']]], axis=1)
    df_combined['optimal_irrigation'] = np.random.uniform(3000000, 5000000, n)  # Optimal irrigation in mm/day

    return df, df_soil, df_combined, df_plant_health
