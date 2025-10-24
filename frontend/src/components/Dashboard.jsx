import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import { Button } from '../components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card'
import { Badge } from '../components/ui/badge'
import { Progress } from '../components/ui/progress'
import { Plus, Flame, CheckCircle, ClipboardList, Check, RotateCcw, Loader2, Clock } from 'lucide-react'

const API_BASE_URL = 'http://localhost:8080/api'

function Dashboard() {
  const [habits, setHabits] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [totalStreak, setTotalStreak] = useState(0)
  const [showDailyReset, setShowDailyReset] = useState(false)
  const [lastResetDate, setLastResetDate] = useState(() => {
    return localStorage.getItem('lastResetDate') || new Date().toDateString()
  })
  const [timeUntilMidnight, setTimeUntilMidnight] = useState('')
  const [cardsRefreshing, setCardsRefreshing] = useState(false)

  // For demo purposes, using userId = 1
  const userId = 1

  useEffect(() => {
    fetchHabits()
    setupMidnightTimer()
    updateTimeUntilMidnight()
    
    // Check if it's a new day
    checkForNewDay()
    
    // Update time every minute
    const interval = setInterval(updateTimeUntilMidnight, 60000)
    
    return () => clearInterval(interval)
  }, [])

  const updateTimeUntilMidnight = () => {
    const now = new Date()
    const midnight = new Date(now)
    midnight.setHours(24, 0, 0, 0)
    
    const diff = midnight.getTime() - now.getTime()
    const hours = Math.floor(diff / (1000 * 60 * 60))
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
    
    setTimeUntilMidnight(`${hours}h ${minutes}m`)
  }

  const checkForNewDay = async () => {
    const today = new Date().toDateString()
    if (today !== lastResetDate) {
      setShowDailyReset(true)
      setLastResetDate(today)
      localStorage.setItem('lastResetDate', today)
      
      // Start card refresh animation
      setCardsRefreshing(true)
      
      // Clear habits first to show removal effect
      setTimeout(async () => {
        setHabits([])
        
        // Remove ALL habits - no recreation, completely fresh start
        try {
          await removeAllHabits()
        } catch (error) {
          console.error('Error during daily reset:', error)
        }
      }, 500)
      
      // Fetch fresh data after cards are "removed" and recreated
      setTimeout(() => {
        fetchHabits()
        setCardsRefreshing(false)
      }, 1500)
      
      // Hide the animation after 5 seconds
      setTimeout(() => {
        setShowDailyReset(false)
      }, 5000)
    }
  }

  const setupMidnightTimer = () => {
    const now = new Date()
    const midnight = new Date(now)
    midnight.setHours(24, 0, 0, 0) // Next midnight
    
    const timeUntilMidnight = midnight.getTime() - now.getTime()
    
    const timer = setTimeout(async () => {
      // It's midnight! Start the refresh process
      setShowDailyReset(true)
      setCardsRefreshing(true)
      
      // Clear habits first to show removal effect
      setTimeout(async () => {
        setHabits([])
        
        // Remove ALL habits - no recreation, completely fresh start
        try {
          await removeAllHabits()
        } catch (error) {
          console.error('Error during daily reset:', error)
        }
      }, 500)
      
      // Fetch fresh data after cards are "removed" and recreated
      setTimeout(() => {
        fetchHabits()
        setCardsRefreshing(false)
      }, 1500)
      
      // Hide the animation after 5 seconds
      setTimeout(() => {
        setShowDailyReset(false)
      }, 5000)
      
      // Set up the next midnight timer
      setupMidnightTimer()
    }, timeUntilMidnight)
    
    return () => clearTimeout(timer)
  }

  const fetchHabits = async () => {
    try {
      setLoading(true)
      const response = await axios.get(`${API_BASE_URL}/habits/${userId}`)
      setHabits(response.data)
      
      // Fetch user streak from backend
      await fetchUserStreak()
    } catch (error) {
      console.error('Error fetching habits:', error)
      setError('Failed to load habits. Please check if the backend is running.')
      setHabits([])
      setTotalStreak(0)
    } finally {
      setLoading(false)
    }
  }

  const fetchUserStreak = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/habits/${userId}/streak`)
      setTotalStreak(response.data)
    } catch (error) {
      console.error('Error fetching user streak:', error)
      setTotalStreak(0)
    }
  }

  const removeAllHabits = async () => {
    try {
      // Get current habits
      const response = await axios.get(`${API_BASE_URL}/habits/${userId}`)
      const currentHabits = response.data
      
      // Delete ALL existing habits - no recreation
      for (const habit of currentHabits) {
        try {
          await axios.delete(`${API_BASE_URL}/habits/${habit.id}`)
        } catch (error) {
          console.error(`Error deleting habit ${habit.id}:`, error)
        }
      }
      
      // Fetch updated streak from backend (backend will handle streak logic)
      await fetchUserStreak()
      
      // Database is now completely clean - no habits remain
      console.log('All habits deleted - database is now completely fresh')
    } catch (error) {
      console.error('Error in removeAllHabits:', error)
    }
  }

  const markHabitDone = async (habitId) => {
    try {
      await axios.post(`${API_BASE_URL}/habits/${habitId}/log`)
      
      // Refresh habits from backend to get updated data
      fetchHabits()
    } catch (error) {
      console.error('Error marking habit as done:', error)
      setError('Failed to mark habit as done. Please try again.')
    }
  }

  const markHabitPartialDone = async (habitId, completedCount) => {
    try {
      await axios.post(`${API_BASE_URL}/habits/${habitId}/log/${completedCount}`)
      
      // Refresh habits from backend to get updated data
      fetchHabits()
    } catch (error) {
      console.error('Error marking habit as partially done:', error)
      setError('Failed to mark habit as partially done. Please try again.')
    }
  }

  const getBadgeForStreak = (streak) => {
    if (streak >= 30) return { name: 'Legend', color: 'bg-purple-500', icon: '👑' }
    if (streak >= 14) return { name: 'Champion', color: 'bg-yellow-500', icon: '🏆' }
    if (streak >= 7) return { name: 'Streak Master', color: 'bg-green-500', icon: '🔥' }
    if (streak >= 3) return { name: 'Getting Started', color: 'bg-blue-500', icon: '⭐' }
    return { name: 'Newbie', color: 'bg-gray-500', icon: '🌱' }
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="h-16 w-16 animate-spin text-indigo-600" />
      </div>
    )
  }

  if (error) {
    return (
      <div className="text-center">
        <div className="text-red-600 mb-4">Error loading habits: {error}</div>
        <Button 
          onClick={() => window.location.reload()} 
          className="bg-indigo-600 text-white hover:bg-indigo-700"
        >
          <RotateCcw className="h-4 w-4 mr-2" />
          Retry
        </Button>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      {/* Daily Reset Loader */}
      {showDailyReset && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Loader2 className="h-16 w-16 animate-spin text-white" />
        </div>
      )}

      {/* Header with Fire Icon and Total Streak */}
      <div className="flex justify-between items-center">
        <div className="flex items-center space-x-4">
          <div className="flex flex-col">
            <div className="flex items-center space-x-3">
              <h1 className="text-4xl font-bold text-black">
                Your Habits
              </h1>
              {totalStreak > 0 && (
                <div className="flex items-center space-x-1 bg-gradient-to-r from-orange-100 to-red-100 px-3 py-1.5 rounded-full">
                  <Flame className="h-4 w-4 text-orange-600" />
                  <span className="font-bold text-orange-600 text-sm">{totalStreak}</span>
                </div>
              )}
            </div>
            <div className="flex items-center space-x-4 text-sm text-gray-500 mt-1">
              <div className="flex items-center space-x-1">
                <Clock className="h-4 w-4 text-orange-500" />
                <span>Last reset: {new Date().toLocaleDateString()}</span>
              </div>
              <div className="flex items-center space-x-1">
                <Flame className="h-4 w-4 text-orange-500" />
                <span>Next reset in: {timeUntilMidnight}</span>
              </div>
            </div>
          </div>
        </div>
        <Button asChild className="bg-black hover:bg-gray-800 text-white">
          <Link to="/add-habit">
            <Plus className="h-5 w-5 mr-2" />
            Add Habit
          </Link>
        </Button>
      </div>

      {habits.length === 0 ? (
        <Card className="text-center py-12 border border-black">
          <CardContent>
            <div className="mx-auto w-24 h-24 bg-gradient-to-br from-indigo-100 to-purple-100 rounded-full flex items-center justify-center mb-6">
              <ClipboardList className="h-12 w-12 text-indigo-500" />
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">Fresh Start!</h3>
            <p className="text-gray-500 mb-6">All habits have been cleared. Create new habits to continue your streak!</p>
            <Button asChild className="bg-black hover:bg-gray-800 text-white">
              <Link to="/add-habit">
                <Plus className="-ml-1 mr-2 h-5 w-5" />
                Create New Habit
              </Link>
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {habits.map((habit) => {
            const badge = getBadgeForStreak(habit.streakCount)
            return (
              <Card 
                key={habit.id} 
                className={`border border-black transition-all duration-500 ${
                  cardsRefreshing 
                    ? 'opacity-0 scale-95 transform -translate-y-4' 
                    : 'opacity-100 scale-100 transform translate-y-0'
                }`}
              >
                <CardHeader className="pb-3">
                  <div className="flex items-start justify-between">
                    <div className="space-y-1">
                      <CardTitle className="text-xl font-bold text-gray-900">
                        {habit.name}
                      </CardTitle>
                      <CardDescription className="text-gray-600">
                        {habit.description}
                      </CardDescription>
                    </div>
                    <Badge 
                      variant="secondary" 
                      className={`${badge.color} text-white border-0 shadow-sm`}
                    >
                      <span className="mr-1">{badge.icon}</span>
                      {badge.name}
                    </Badge>
                  </div>
                </CardHeader>
                
                <CardContent className="space-y-6">
                  {/* Stats */}
                  <div className="grid grid-cols-2 gap-4">
                    <div className="text-center p-3 bg-gradient-to-br from-blue-50 to-indigo-50 rounded-lg">
                      <div className="text-2xl font-bold text-blue-600">{habit.streakCount}</div>
                      <div className="text-sm text-blue-500 font-medium">Days Streak</div>
                    </div>
                    <div className="text-center p-3 bg-gradient-to-br from-green-50 to-emerald-50 rounded-lg">
                      <div className="text-2xl font-bold text-green-600">
                        {habit.completedToday ? '100' : Math.round(habit.progressPercent)}%
                      </div>
                      <div className="text-sm text-green-500 font-medium">Progress</div>
                    </div>
                  </div>

                          {/* Progress Bar */}
                          <div className="space-y-2">
                            <div className="flex justify-between text-sm">
                              <span className="text-gray-600">Today's Progress</span>
                              <span className="font-medium text-gray-900">
                                {habit.completedCountToday || 0}/{habit.dailyGoal} ({habit.completedToday ? '100' : Math.round(habit.progressPercent)}%)
                              </span>
                            </div>
                            <Progress 
                              value={habit.completedToday ? 100 : habit.progressPercent} 
                              className="h-3"
                            />
                          </div>

                  {/* Action Buttons */}
                  <div className="pt-2">
                    {habit.completedToday ? (
                      <div className="flex items-center justify-center space-x-2 text-green-600 bg-green-50 py-3 px-4 rounded-lg">
                        <Check className="h-5 w-5" />
                        <span className="font-semibold">Completed Today!</span>
                      </div>
                    ) : habit.dailyGoal > 1 ? (
                      <div className="space-y-2">
                        <div className="text-sm text-gray-600 text-center mb-2">
                          Mark progress: {habit.completedCountToday || 0}/{habit.dailyGoal}
                        </div>
                        <div className="grid grid-cols-2 gap-2">
                          {Array.from({ length: habit.dailyGoal }, (_, i) => i + 1).map((count) => (
                            <Button
                              key={count}
                              onClick={() => markHabitPartialDone(habit.id, count)}
                              variant={habit.completedCountToday >= count ? "default" : "outline"}
                              className={`text-sm py-2 ${
                                habit.completedCountToday >= count 
                                  ? "bg-gradient-to-r from-orange-500 to-red-500 hover:from-orange-600 hover:to-red-600 text-white" 
                                  : "border-orange-300 text-orange-600 hover:bg-orange-50"
                              }`}
                            >
                              {count}
                            </Button>
                          ))}
                        </div>
                      </div>
                    ) : (
                      <Button
                        onClick={() => markHabitDone(habit.id)}
                        className="w-full bg-gradient-to-r from-orange-500 to-red-500 hover:from-orange-600 hover:to-red-600 text-white font-semibold py-3"
                      >
                        <Check className="h-5 w-5 mr-2" />
                        Mark as Done
                      </Button>
                    )}
                  </div>
                </CardContent>
              </Card>
            )
          })}
        </div>
      )}
    </div>
  )
}

export default Dashboard
