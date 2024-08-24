package com.example.tvzprojekt.Controllers;

public sealed interface SearchBar permits EventsController, ProfesoriController, StudentiController, TransakcijeController{
    void searchForValue();
    void clearValues();
}
