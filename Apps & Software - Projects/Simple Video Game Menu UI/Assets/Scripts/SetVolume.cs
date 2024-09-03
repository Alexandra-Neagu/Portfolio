using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SetVolume : MonoBehaviour {

    private AudioSource mainMenuMusic;
    public Slider volumeSlider;

	// Use this for initialization
	void Start () {
        mainMenuMusic = gameObject.GetComponent<AudioSource>();
	}
	
	// Update is called once per frame
	void Update () {
        mainMenuMusic.volume = volumeSlider.value;
	}
}
