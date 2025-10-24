import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import { Button } from '../components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card'
import { Plus, Loader2 } from 'lucide-react'

const API_BASE_URL = 'http://localhost:8080/api'

function AddHabit() {
  const navigate = useNavigate()
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    dailyGoal: 1,
    userId: 1 // For demo purposes
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      await axios.post(`${API_BASE_URL}/habits`, formData)
      navigate('/dashboard')
    } catch (error) {
      console.error('Error creating habit:', error)
      if (error.response?.status === 400) {
        setError('Please check your input and try again.')
      } else {
        setError('Failed to create habit. Please check if the backend is running.')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto space-y-8">
      <div className="text-center">
                <h1 className="text-4xl font-bold text-black mb-4">
                  Create New Habit
                </h1>
        <p className="text-lg text-gray-600">
          Start your journey towards building better habits and achieving your goals
        </p>
      </div>

      <Card className="border border-black">
        <CardHeader>
          <CardTitle className="text-2xl text-center">Habit Details</CardTitle>
          <CardDescription className="text-center">
            Fill in the details to create your new habit
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label htmlFor="name" className="text-sm font-medium text-gray-700">
                Habit Name *
              </label>
              <input
                type="text"
                name="name"
                id="name"
                required
                value={formData.name}
                onChange={handleChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors"
                placeholder="e.g., Drink Water, Exercise, Read"
              />
            </div>

            <div className="space-y-2">
              <label htmlFor="description" className="text-sm font-medium text-gray-700">
                Description
              </label>
              <textarea
                name="description"
                id="description"
                rows={3}
                value={formData.description}
                onChange={handleChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors resize-none"
                placeholder="Describe your habit goal and why it's important to you..."
              />
            </div>

            <div className="space-y-2">
              <label htmlFor="dailyGoal" className="text-sm font-medium text-gray-700">
                Daily Goal
              </label>
              <select
                name="dailyGoal"
                id="dailyGoal"
                value={formData.dailyGoal}
                onChange={handleChange}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors"
              >
                <option value={1}>Once per day</option>
                <option value={2}>Twice per day</option>
                <option value={3}>Three times per day</option>
                <option value={4}>Four times per day</option>
                <option value={5}>Five times per day</option>
              </select>
              <p className="text-sm text-gray-500">
                How many times do you want to complete this habit each day?
              </p>
            </div>

            {error && (
              <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                <div className="flex">
                  <div className="flex-shrink-0">
                    <svg className="h-5 w-5 text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                  </div>
                  <div className="ml-3">
                    <p className="text-sm text-red-800">{error}</p>
                  </div>
                </div>
              </div>
            )}

            <div className="flex space-x-4 pt-4">
              <Button
                type="button"
                variant="outline"
                onClick={() => navigate('/dashboard')}
                className="flex-1"
              >
                Cancel
              </Button>
                      <Button
                        type="submit"
                        disabled={loading}
                        className="flex-1 bg-black hover:bg-gray-800 text-white"
                      >
                {loading ? (
                  <div className="flex items-center justify-center">
                    <Loader2 className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" />
                    Creating...
                  </div>
                ) : (
                  'Create Habit'
                )}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>

    </div>
  )
}

export default AddHabit
