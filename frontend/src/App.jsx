import React, { useState, useEffect } from 'react'
import { Routes, Route, Link, useLocation } from 'react-router-dom'
import Home from './components/Home'
import Dashboard from './components/Dashboard'
import AddHabit from './components/AddHabit'
import { Button } from './components/ui/button'
import { Plus } from 'lucide-react'
import axios from 'axios'
import './App.css'

function App() {
  const [totalStreak, setTotalStreak] = useState(0)
  const location = useLocation()
  const API_BASE_URL = 'http://localhost:8080/api'

  useEffect(() => {
    const fetchTotalStreak = async () => {
      try {
        const response = await axios.get(`${API_BASE_URL}/habits/1`)
        const total = response.data.reduce((sum, habit) => sum + (habit.streakCount || 0), 0)
        setTotalStreak(total)
      } catch (error) {
        setTotalStreak(0)
      }
    }
    
    fetchTotalStreak()
  }, [location.pathname])

  return (
    <div className="min-h-screen bg-white">
      <nav className="bg-white/80 backdrop-blur-md border-b border-white/20 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center space-x-4">
              <Link to="/" className="text-2xl font-bold text-black">
                Habit Tracker
              </Link>
              {totalStreak > 0 && (
                <div className="flex items-center space-x-2 bg-gradient-to-r from-orange-100 to-red-100 px-3 py-1.5 rounded-full">
                  <span className="text-lg">🔥</span>
                  <span className="font-bold text-orange-600 text-sm">{totalStreak}</span>
                </div>
              )}
            </div>
            <div className="flex items-center space-x-2">
              <Button variant="ghost" asChild>
                <Link to="/" className={location.pathname === '/' ? 'bg-indigo-100 text-indigo-700' : ''}>
                  Home
                </Link>
              </Button>
              <Button variant="ghost" asChild>
                <Link to="/dashboard" className={location.pathname === '/dashboard' ? 'bg-indigo-100 text-indigo-700' : ''}>
                  Dashboard
                </Link>
              </Button>
              <Button asChild className="bg-black hover:bg-gray-800 text-white">
                <Link to="/add-habit">
                  <Plus className="h-4 w-4 mr-2" />
                  Add Habit
                </Link>
              </Button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto py-8 sm:px-6 lg:px-8">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/add-habit" element={<AddHabit />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
