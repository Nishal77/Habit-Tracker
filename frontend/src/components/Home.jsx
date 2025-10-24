import React from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { Card, CardContent } from '../components/ui/card'
import { CheckCircle, Flame, Sparkles, BarChart3, Plus } from 'lucide-react'

function Home() {
  return (
    <div className="text-center space-y-16">
      {/* Hero Section */}
      <div className="max-w-4xl mx-auto space-y-8">
        <div className="space-y-6">
          <h1 className="text-5xl md:text-7xl font-bold text-black">
            Build Better Habits
          </h1>
          <p className="text-xl md:text-2xl text-gray-600 max-w-3xl mx-auto leading-relaxed">
            Track your daily habits, build powerful streaks, and achieve your goals with our beautiful and intuitive habit tracker.
          </p>
        </div>
        
        <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
          <Button asChild size="lg" className="bg-black hover:bg-gray-800 text-white text-lg px-8 py-4">
            <Link to="/dashboard">
              <BarChart3 className="h-5 w-5 mr-2" />
              Go to Dashboard
            </Link>
          </Button>
          <Button asChild variant="outline" size="lg" className="text-lg px-8 py-4 border-2">
            <Link to="/add-habit">
              <Plus className="h-5 w-5 mr-2" />
              Add Your First Habit
            </Link>
          </Button>
        </div>
      </div>

      {/* Features Section */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
        <Card className="border border-black">
          <CardContent className="p-8 text-center">
            <div className="w-16 h-16 bg-gradient-to-br from-green-400 to-emerald-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <CheckCircle className="h-8 w-8 text-white" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900 mb-4">Track Progress</h3>
            <p className="text-gray-600 leading-relaxed">
              Monitor your daily habits with beautiful progress bars and visual indicators that show your consistency over time.
            </p>
          </CardContent>
        </Card>

        <Card className="border border-black">
          <CardContent className="p-8 text-center">
            <div className="w-16 h-16 bg-gradient-to-br from-orange-400 to-red-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <Flame className="h-8 w-8 text-white" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900 mb-4">Build Streaks</h3>
            <p className="text-gray-600 leading-relaxed">
              Maintain consistency and build powerful streaks that motivate you to keep going every single day.
            </p>
          </CardContent>
        </Card>

        <Card className="border border-black">
          <CardContent className="p-8 text-center">
            <div className="w-16 h-16 bg-gradient-to-br from-purple-400 to-pink-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <Sparkles className="h-8 w-8 text-white" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900 mb-4">Earn Badges</h3>
            <p className="text-gray-600 leading-relaxed">
              Unlock achievements and badges as you reach milestones and maintain your habits consistently.
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

export default Home
