import numpy as np

# Constants for the Penman-Monteith equation
gamma = 0.066  # Psychrometric constant (kPa/°C)
Cn = 900  # Constant for daily time step
Cd = 0.34  # Constant for daily time step

def saturation_vapor_pressure(temp):
    return 0.6108 * np.exp((17.27 * temp) / (temp + 237.3))

def slope_saturation_vapor_pressure_curve(temp):
    return (4098 * saturation_vapor_pressure(temp)) / ((temp + 237.3) ** 2)

def actual_vapor_pressure(dew_point):
    return saturation_vapor_pressure(dew_point)

def hargreaves_ET(t_max, t_min, t_mean, R_a):
    return 0.0023 * R_a * (t_max - t_min) ** 0.5 * (t_mean + 17.8)

def compute_penman_monteith(df):
    df['e_s'] = saturation_vapor_pressure(df['ambient_temperature'])
    df['e_a'] = actual_vapor_pressure(df['dew_point'])
    df['delta'] = slope_saturation_vapor_pressure_curve(df['ambient_temperature'])
    df['Rn'] = df['solar_radiation']  # Assume net radiation is equal to solar radiation for simplicity
    df['G'] = 0  # Soil heat flux density is assumed to be 0 for simplicity
    df['ET0'] = (0.408 * df['delta'] * (df['Rn'] - df['G']) + gamma * (Cn / (df['ambient_temperature'] + 273)) * df['wind_speed'] * (df['e_s'] - df['e_a'])) / (df['delta'] + gamma * (1 + Cd * df['wind_speed']))
    return df

def compute_hargreaves(df):
    df['t_max'] = df['ambient_temperature'] + np.random.uniform(-5, 5, len(df))  # Dummy max temp
    df['t_min'] = df['ambient_temperature'] + np.random.uniform(-10, 0, len(df))  # Dummy min temp
    df['t_mean'] = (df['t_max'] + df['t_min']) / 2
    df['R_a'] = df['solar_radiation']  # Assuming R_a is solar radiation
    df['ET0'] = hargreaves_ET(df['t_max'], df['t_min'], df['t_mean'], df['R_a'])
    return df

def compute_ET(df, method='Penman-Monteith'):
    if method == 'Penman-Monteith':
        df = compute_penman_monteith(df)
    elif method == 'Hargreaves':
        df = compute_hargreaves(df)
    else:
        raise ValueError("Unsupported ET method. Choose 'Penman-Monteith' or 'Hargreaves'.")
    
    # Compute real ET using crop coefficient (Kc)
    df['ETc'] = df['ET0'] * df['crop_coefficient']
    
    return df
